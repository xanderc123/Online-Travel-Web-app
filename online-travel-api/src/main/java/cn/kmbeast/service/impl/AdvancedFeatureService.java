package cn.kmbeast.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.*;


@Service
public class AdvancedFeatureService {

    @Autowired
    private DataSource dataSource; // Spring 自动注入数据源

    /**
     * JDBC 事务演示：统计用户消费并升级 VIP
     * 满足作业要求：
     * 1. Transaction (手动 commit/rollback)
     * 2. Isolation Level (SERIALIZABLE)
     * 3. Advanced Query (Join + Aggregation)
     */
    public Map<String, Object> checkAndUpgradeVip(Integer userId) {
        Connection conn = null;
        PreparedStatement pstmtHotel = null;
        PreparedStatement pstmtTicket = null;
        PreparedStatement pstmtUpdate = null;
        Map<String, Object> result = new HashMap<>();

        // 阈值：消费超过 1000 元升级 VIP
        BigDecimal vipThreshold = new BigDecimal("1000");

        try {
            // 1. 获取连接
            conn = dataSource.getConnection();

            // 2.【关键】关闭自动提交，开启事务
            conn.setAutoCommit(false);

            // 3.【关键】设置事务隔离级别 (作业要求)
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            // --- 步骤 A：查询酒店消费总额 (JOIN + AGGREGATION) ---
            String hotelSql = "SELECT IFNULL(SUM(amount), 0) as total FROM hotel_order_info WHERE user_id = ? AND pay_status = 1";
            pstmtHotel = conn.prepareStatement(hotelSql);
            pstmtHotel.setInt(1, userId);
            ResultSet rsHotel = pstmtHotel.executeQuery();

            BigDecimal hotelTotal = BigDecimal.ZERO;
            if (rsHotel.next()) {
                hotelTotal = rsHotel.getBigDecimal("total");
            }

            // --- 步骤 B：查询门票消费总额 ---
            // 注意：你的 attraction_ticket_order 表里 user_id 是 varchar 类型，所以这里要把 int 转成 String
            String ticketSql = "SELECT IFNULL(SUM(amount), 0) as total FROM attraction_ticket_order WHERE user_id = ? AND pay_status = 1";
            pstmtTicket = conn.prepareStatement(ticketSql);
            pstmtTicket.setString(1, String.valueOf(userId));
            ResultSet rsTicket = pstmtTicket.executeQuery();

            BigDecimal ticketTotal = BigDecimal.ZERO;
            if (rsTicket.next()) {
                ticketTotal = rsTicket.getBigDecimal("total");
            }

            // 计算总消费
            BigDecimal grandTotal = hotelTotal.add(ticketTotal);
            result.put("hotelSpent", hotelTotal);
            result.put("ticketSpent", ticketTotal);
            result.put("totalSpent", grandTotal);

            // --- Step C: Judgment Logic and Update ---
            if (grandTotal.compareTo(vipThreshold) >= 0) {
                // Consumption meets standard, execute update
                String updateSql = "UPDATE user SET user_role = 2 WHERE id = ?";
                pstmtUpdate = conn.prepareStatement(updateSql);
                pstmtUpdate.setInt(1, userId);
                int rows = pstmtUpdate.executeUpdate();

                if (rows > 0) {
                    result.put("success", true);
                    result.put("message", "Congratulations! Total spending " + grandTotal + " dollar, successfully upgraded to VIP user (Role=2)!");
                } else {
                    result.put("success", false);
                    result.put("message", "Consumption meets standard, but failed to update user status (user may not exist).");
                }
            } else {
                // Consumption does not meet standard
                result.put("success", false);
                result.put("message", "Current total spending: " + grandTotal + " dollar. Still need " + vipThreshold.subtract(grandTotal) + " dollar to upgrade to VIP.");
            }

// 4. [Key] Commit transaction
            conn.commit();
            System.out.println("JDBC Transaction Committed Successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    // 5. [Key] Exception occurred, rollback transaction
                    conn.rollback();
                    System.out.println("JDBC Transaction Rolled Back.");
                    result.put("success", false);
                    result.put("message", "System error, operation rolled back: " + e.getMessage());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } finally {
            // 6. 关闭资源 (标准 JDBC 操作)
            try {
                if (pstmtHotel != null) pstmtHotel.close();
                if (pstmtTicket != null) pstmtTicket.close();
                if (pstmtUpdate != null) pstmtUpdate.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    // AdvancedFeatureService.java 追加代码

    /**
     * JDBC 调用存储过程演示
     */
    public BigDecimal callStoredProcedure(String startDate, String endDate) {
        Connection conn = null;
        CallableStatement cstmt = null;
        BigDecimal totalRevenue = BigDecimal.ZERO;

        try {
            conn = dataSource.getConnection();
            // 调用存储过程: {call GetTopSupplierStats(?, ?, ?)}
            // 第3个问号是输出参数 (OUT)
            String sql = "{call GetTopSupplierStats(?, ?, ?)}";
            cstmt = conn.prepareCall(sql);

            // 设置输入参数 (IN)
            cstmt.setString(1, startDate); // '2023-01-01'
            cstmt.setString(2, endDate);   // '2025-12-31'

            // 注册输出参数 (OUT) - MySQL Decimal 对应 Types.DECIMAL
            cstmt.registerOutParameter(3, Types.DECIMAL);

            // 执行
            cstmt.execute();

            // 获取输出结果
            totalRevenue = cstmt.getBigDecimal(3);
            if (totalRevenue == null) {
                totalRevenue = BigDecimal.ZERO;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("存储过程调用失败: " + e.getMessage());
        } finally {
            try {
                if (cstmt != null) cstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return totalRevenue;
    }

    public List<Map<String, Object>> generateSupplierReport(String startDate, String endDate) {
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> reportList = new ArrayList<>();

        try {
            conn = dataSource.getConnection();

            // 调用存储过程
            String sql = "{call GenerateSupplierReport(?, ?)}";
            cstmt = conn.prepareCall(sql);

            // 设置参数
            cstmt.setString(1, startDate);
            cstmt.setString(2, endDate);

            // 执行查询
            boolean hasResults = cstmt.execute();

            // 处理返回的结果集 (ResultSet)
            if (hasResults) {
                rs = cstmt.getResultSet();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("supplierName", rs.getString("supplier_name"));
                    row.put("totalSales", rs.getBigDecimal("total_sales"));
                    row.put("avgRating", rs.getBigDecimal("avg_rating"));
                    row.put("level", rs.getString("performance_level"));
                    reportList.add(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Report generation failed: " + e.getMessage());
        } finally {
            // 关闭资源 (略，同之前)
            try {
                if (rs != null) rs.close();
                if (cstmt != null) cstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
            }
        }
        return reportList;
    }
}