package com.commerce.controllers;

import com.commerce.models.User;
import com.commerce.models.Category;
import com.commerce.models.dto.CategoryDTO;
import com.commerce.services.CategoryService;
import com.commerce.services.ResponseType;
import com.commerce.utils.SessionUtil;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.ArrayList;

public class CategoryController implements Controller{

    private CategoryService categoryService = new CategoryService();

    public CategoryController(){}

    private final Handler viewAllCategories = ctx -> {
        ArrayList<Category> list = categoryService.getAllCategories();

        ctx.json(list);
        ctx.status(200);
    };

    private final Handler createCategory = ctx -> {
        User u = SessionUtil.UserValidate(ctx, User.AccountType.ADMINISTRATOR);
        if (u != null) {

            CategoryDTO dto = ctx.bodyAsClass(CategoryDTO.class);
            Category c = new Category(dto.name, dto.description);

            ResponseType result = categoryService.createCategory(c);

            if (result == ResponseType.SUCCESS){
                ctx.html("CATEGORY_CREATED: " + dto.name);
                ctx.status(200);
            } else {
                ctx.html("ERROR CREATING CATEGORY: " + result.name());
                ctx.status(400);
            }
        }
    };

    @Override
    public void addRoutes(Javalin app) {

        app.get("/categories", viewAllCategories);
        app.post("/categories/create", createCategory);
    }
}
