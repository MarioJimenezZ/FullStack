package com.commerce.controllers;

import com.commerce.models.Cart;
import com.commerce.models.User;
import com.commerce.models.Product;
import com.commerce.models.dto.ProductDTO;
import com.commerce.services.CartService;
import com.commerce.services.CategoryService;
import com.commerce.services.ProductService;
import com.commerce.services.ResponseType;
import com.commerce.utils.SessionUtil;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ProductController implements Controller{

    private ProductService productService = new ProductService();
    private CategoryService categoryService = new CategoryService();
    private CartService cartService = new CartService();

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    public ProductController(){}

    private final Handler getAllProducts = ctx -> {
        ArrayList<Product> list = productService.getAllProducts();

        ctx.json(list);
        ctx.status(200);
    };

    private final Handler getProduct = ctx -> {
        String pathId = ctx.pathParam("id");
        int id = Integer.parseInt(pathId);

        Product p = productService.getProductById(id);
        if (p != null) {
            ctx.json(p);
            ctx.status(200);
        } else {
            ctx.html("<h1>ERROR_GETTING_PRODUCT_ID : " + id + "</h1>");
            ctx.status(400);
            log.warn("ERROR_GETTING_PRODUCT_ID : " + id);
        }
    };

    private final Handler addProduct = ctx -> {
        // Checks if user is at least a seller or higher
        User u = SessionUtil.UserValidate(ctx, User.AccountType.SELLER);
        if (u != null) {
            // Creates Data Transfer Object
            ProductDTO dto = ctx.bodyAsClass(ProductDTO.class);
            // Creates new product with info
            Product p = new Product(dto.name, dto.categoryId,
                    dto.description, dto.price, u.getId());
            // Attempts to add product to database
            ResponseType result = productService.addProduct(p);
            // If we succeed
            if (result == ResponseType.SUCCESS) {
                ctx.status(201);
                ctx.html("<h1> ADDED_PRODUCT : " + p.getName() + "</h1>");
                log.info("ADDED PRODUCT TO DATABASE: \n " + p);
            } else {
                ctx.html("<h1>ERROR_ADDING_PRODUCT : " + result.name() + "</h1>");
                ctx.status(400);
                log.warn("ERROR_ADDING_PRODUCT : \n" + p + " \n" + result.name());
            }
        }
    };

    private final Handler addToCart = ctx -> {
        // Checks if user is logged in
        User u = SessionUtil.UserValidate(ctx, User.AccountType.CUSTOMER);
        if (u != null) {
            String pathId = ctx.pathParam("id");
            int id = Integer.parseInt(pathId);

            Product p = productService.getProductById(id);

            if (p != null) {
                // Checks if user has cart
                Cart user_cart = cartService.getCartByUserId(u.getId());
                if (user_cart == null) {
                    user_cart = new Cart();
                    user_cart.setUserId(u.getId());
                    cartService.createCart(user_cart);
                    user_cart = cartService.getCartByUserId(u.getId());

                }
                user_cart.addProduct(p);
                ResponseType result = cartService.updateCart(user_cart);
                // If we succeed
                if (result == ResponseType.SUCCESS) {
                    ctx.status(201);
                    ctx.html("<h1> ADDED_PRODUCT_TO_CART : " + p.getName() + "</h1>");
                    log.info("ADDED PRODUCT TO CART: \n " + p + "\n" + user_cart);
                } else {
                    ctx.html("<h1>ERROR_ADDING_PRODUCT_TO_CART : " + result.name() + "</h1>");
                    ctx.status(400);
                    log.warn("ERROR_ADDING_PRODUCT_TO_CART : \n" + p + " \n" + result.name());
                }

            } else {
                ctx.html("<h1>CANNOT_FIND_PRODUCT_ID: </h1>" + id);
                ctx.status(400);
                log.warn("User trying to view product with invalid id : " + u.getEmail());
            }

        }
    };

    @Override
    public void addRoutes(Javalin app) {
        // Views all products
        app.get("/products", getAllProducts);
        // Views product by Id
        app.get("/product/{id}", getProduct);
        // Adds Product to Cart
        app.put("/product/{id}", addToCart);

        // Creates new product
        app.post("/product/add", addProduct);
    }
}
