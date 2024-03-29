package com.ers.repos;

import com.ers.models.Reimbursement;
import com.ers.models.User;
import com.ers.utils.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementDAOImpl implements ReimbursementDAO {

    private final Logger log = LoggerFactory.getLogger(ReimbursementDAOImpl.class);

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public List<Reimbursement> getAllByUserId(int userId) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM ers_reimbursement WHERE reimb_author = ?;";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, userId);

            ResultSet result = statement.executeQuery();

            List<Reimbursement> list = new ArrayList<>();

            while(result.next()){
                Reimbursement reimb = new Reimbursement();
                reimb.setId(result.getInt("reimb_id"));
                reimb.setAmount(result.getDouble("reimb_amount"));
                reimb.setDateSubmitted(result.getTimestamp("reimb_submitted"));
                reimb.setDateResolved(result.getTimestamp("reimb_resolved"));
                reimb.setDescription(result.getString("reimb_description"));
                reimb.setAuthorId(result.getInt("reimb_author"));
                reimb.setResolverId(result.getInt("reimb_resolver"));
                reimb.setStatusId(result.getInt("reimb_status_id"));
                reimb.setTypeId(result.getInt("reimb_type_id"));

                String authorName = "";
                String resolverName = "";

                User userAuthor = userDAO.get(reimb.getAuthorId());

                byte[] receipt = result.getBytes("reimb_receipt");
                if (receipt != null && receipt.length > 0) {
                    reimb.setReceipt(new String(receipt, StandardCharsets.UTF_8));
                }

                if(reimb.getResolverId() != 0) {
                    User userResolver = userDAO.get(reimb.getResolverId());
                    resolverName += userResolver.getFirstName() + " " + userResolver.getLastName();
                    reimb.setFullNameResolver(resolverName);
                }
                authorName += userAuthor.getFirstName() + " " + userAuthor.getLastName();
                reimb.setFullNameAuthor(authorName);

                list.add(reimb);
            }

            return list;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Reimbursement> getAllByStatus(int statusId) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM ers_reimbursement WHERE reimb_status_id = ?;";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, statusId);

            ResultSet result = statement.executeQuery();

            List<Reimbursement> list = new ArrayList<>();

            while(result.next()){
                Reimbursement reimb = new Reimbursement();
                reimb.setId(result.getInt("reimb_id"));
                reimb.setAmount(result.getDouble("reimb_amount"));
                reimb.setDateSubmitted(result.getTimestamp("reimb_submitted"));
                reimb.setDateResolved(result.getTimestamp("reimb_resolved"));
                reimb.setDescription(result.getString("reimb_description"));
                reimb.setAuthorId(result.getInt("reimb_author"));
                reimb.setResolverId(result.getInt("reimb_resolver"));
                reimb.setStatusId(result.getInt("reimb_status_id"));
                reimb.setTypeId(result.getInt("reimb_type_id"));

                String authorName = "";
                String resolverName = "";

                User userAuthor = userDAO.get(reimb.getAuthorId());

                byte[] receipt = result.getBytes("reimb_receipt");
                if (receipt != null && receipt.length > 0) {
                    reimb.setReceipt(new String(receipt, StandardCharsets.UTF_8));
                }

                if(reimb.getResolverId() != 0) {
                    User userResolver = userDAO.get(reimb.getResolverId());
                    resolverName += userResolver.getFirstName() + " " + userResolver.getLastName();
                    reimb.setFullNameResolver(resolverName);
                }
                authorName += userAuthor.getFirstName() + " " + userAuthor.getLastName();
                reimb.setFullNameAuthor(authorName);

                list.add(reimb);
            }

            return list;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Reimbursement getByTimestamp(Timestamp t) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM ers_reimbursement WHERE reimb_submitted = ?; ";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setTimestamp(1, t);

            ResultSet result = statement.executeQuery();

            if(result.next()){
                Reimbursement reimb = new Reimbursement();
                reimb.setId(result.getInt("reimb_id"));
                reimb.setAmount(result.getDouble("reimb_amount"));
                reimb.setDateSubmitted(result.getTimestamp("reimb_submitted"));
                reimb.setDateResolved(result.getTimestamp("reimb_resolved"));
                reimb.setDescription(result.getString("reimb_description"));
                reimb.setAuthorId(result.getInt("reimb_author"));
                reimb.setResolverId(result.getInt("reimb_resolver"));
                reimb.setStatusId(result.getInt("reimb_status_id"));
                reimb.setTypeId(result.getInt("reimb_type_id"));


                return reimb;
            }

            return null;

        }catch (SQLException e){
            System.out.println(e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteByTimestamp(Timestamp t) {
        try(Connection conn = ConnectionUtil.getConnection()) {

            String sql = "DELETE FROM ers_reimbursement WHERE reimb_submitted = ?; ";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setTimestamp(1, t);

            statement.execute();

            return true;

        } catch (SQLException e) {
            log.warn("Error while deleting reimbursement timestamp : " + t);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean save(Reimbursement r) {

        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "INSERT INTO ers_reimbursement (reimb_amount, reimb_submitted," +
                    "reimb_description, reimb_receipt, reimb_author, reimb_status_id, reimb_type_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement statement = conn.prepareStatement(sql);
            int count = 0;
            statement.setDouble(++count, r.getAmount());
            statement.setTimestamp(++count, r.getDateSubmitted());
            statement.setString(++count, r.getDescription());
            statement.setBytes(++count, r.getReceipt().getBytes(StandardCharsets.UTF_8));
            statement.setInt(++count, r.getAuthorId());
            statement.setInt(++count, r.getStatusId());
            statement.setInt(++count, r.getTypeId());

            statement.execute();

            return true;

        }catch(SQLException e){
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Reimbursement r) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "UPDATE ers_reimbursement SET reimb_amount = ?, reimb_submitted = ?, " +
                    "reimb_resolved = ?, reimb_description = ?, reimb_receipt = ?," +
                    " reimb_author = ?, reimb_resolver = ?, reimb_status_id = ?, reimb_type_id = ?" +
                    "WHERE reimb_id = ?;";

            PreparedStatement statement = conn.prepareStatement(sql);

            int count = 0;
            statement.setDouble(++count, r.getAmount());
            statement.setTimestamp(++count, r.getDateSubmitted());
            statement.setTimestamp(++count, r.getDateResolved());
            statement.setString(++count, r.getDescription());
            if (r.getReceipt() == null || r.getReceipt().isEmpty()) {
                statement.setBytes(++count, new byte[0]);
            } else {
                statement.setBytes(++count, r.getReceipt().getBytes(StandardCharsets.UTF_8));
            }
            statement.setInt(++count, r.getAuthorId());
            statement.setInt(++count, r.getResolverId());
            statement.setInt(++count, r.getStatusId());
            statement.setInt(++count, r.getTypeId());
            statement.setInt(++count, r.getId());

            statement.execute();

            return true;


        }catch(SQLException e){
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try(Connection conn = ConnectionUtil.getConnection()) {

            StringBuffer sql = new StringBuffer("DELETE FROM ers_reimbursement WHERE reimb_id = " + id + ";");
            Statement statement = conn.createStatement();

            statement.executeUpdate(sql.toString());

            return true;

        } catch (SQLException e) {
            log.warn("Error while deleting reimbursement id : " + id);
            log.error(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Reimbursement> getAll() {

        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM ers_reimbursement;";

            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(sql);

            List<Reimbursement> list = new ArrayList<>();

            while(result.next()){
                Reimbursement reimb = new Reimbursement();
                reimb.setId(result.getInt("reimb_id"));
                reimb.setAmount(result.getDouble("reimb_amount"));
                reimb.setDateSubmitted(result.getTimestamp("reimb_submitted"));
                reimb.setDateResolved(result.getTimestamp("reimb_resolved"));
                reimb.setDescription(result.getString("reimb_description"));
                reimb.setAuthorId(result.getInt("reimb_author"));
                reimb.setResolverId(result.getInt("reimb_resolver"));
                reimb.setStatusId(result.getInt("reimb_status_id"));
                reimb.setTypeId(result.getInt("reimb_type_id"));

                String authorName = "";
                String resolverName = "";

                byte[] receipt = result.getBytes("reimb_receipt");
                if (receipt != null && receipt.length > 0) {
                    reimb.setReceipt(new String(receipt, StandardCharsets.UTF_8));
                }

                User userAuthor = userDAO.get(reimb.getAuthorId());

                System.out.println(reimb.getResolverId());

                if(reimb.getResolverId() != 0) {
                    User userResolver = userDAO.get(reimb.getResolverId());
                    resolverName += userResolver.getFirstName() + " " + userResolver.getLastName();
                    reimb.setFullNameResolver(resolverName);
                }
                authorName += userAuthor.getFirstName() + " " + userAuthor.getLastName();
                reimb.setFullNameAuthor(authorName);


                list.add(reimb);
            }

            return list;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Reimbursement get(int id) {
        try(Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM ers_reimbursement WHERE reimb_id = " + id + ";";

            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(sql);

            if(result.next()){
                Reimbursement reimb = new Reimbursement();
                reimb.setId(result.getInt("reimb_id"));
                reimb.setAmount(result.getDouble("reimb_amount"));
                reimb.setDateSubmitted(result.getTimestamp("reimb_submitted"));
                reimb.setDateResolved(result.getTimestamp("reimb_resolved"));
                reimb.setDescription(result.getString("reimb_description"));

                reimb.setAuthorId(result.getInt("reimb_author"));
                reimb.setResolverId(result.getInt("reimb_resolver"));
                reimb.setStatusId(result.getInt("reimb_status_id"));
                reimb.setTypeId(result.getInt("reimb_type_id"));

                byte[] receipt = result.getBytes("reimb_receipt");
                if (receipt != null && receipt.length > 0) {
                    reimb.setReceipt(new String(receipt, StandardCharsets.UTF_8));
                }

                String authorName = "";
                String resolverName = "";

                User userAuthor = userDAO.get(reimb.getAuthorId());

                System.out.println(reimb.getResolverId());

                if(reimb.getResolverId() != 0) {
                    User userResolver = userDAO.get(reimb.getResolverId());
                    resolverName += userResolver.getFirstName() + " " + userResolver.getLastName();
                    reimb.setFullNameResolver(resolverName);
                }
                authorName += userAuthor.getFirstName() + " " + userAuthor.getLastName();
                reimb.setFullNameAuthor(authorName);

                return reimb;
            }

            return null;

        }catch (SQLException e){
            System.out.println(e);
            e.printStackTrace();
        }
        return null;
    }
}
