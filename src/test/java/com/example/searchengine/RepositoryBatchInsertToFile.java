package com.example.searchengine;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositoryBatchInsertToFile {
    // 数据库连接配置
    private static final String DB_URL = "jdbc:mysql://192.168.100.232:3306/search_engine";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "bite@123";
    private static final String OUTPUT_FILE = "repository_batch_insert.sql";
    private static final int BATCH_SIZE = 500;

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        FileWriter fileWriter = null;

        try {
            // 1. 注册JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 打开连接
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 3. 创建输出文件
            fileWriter = new FileWriter(OUTPUT_FILE);

            // 写入文件头
//            fileWriter.write("-- 批量插入repository表的SQL脚本\n");
//            fileWriter.write("-- 生成时间: " + new java.util.Date() + "\n\n");
//            fileWriter.write("SET FOREIGN_KEY_CHECKS = 0;\n");
//            fileWriter.write("SET UNIQUE_CHECKS = 0;\n");
//            fileWriter.write("SET AUTOCOMMIT = 0;\n\n");

            // 4. 执行查询
            stmt = conn.createStatement();
            String sql = "SELECT id, fullName, htmlUrl, readme, repositoryId FROM repository";
            rs = stmt.executeQuery(sql);

            // 5. 处理结果集并生成批量插入语句
            List<String> batchValues = new ArrayList<>();
            int batchCount = 0;
            int totalProcessed = 0;

            while (rs.next()) {
                // 获取记录
                long id = rs.getLong("id");
                String fullName = escapeSql(rs.getString("fullName"));
                String htmlUrl = escapeSql(rs.getString("htmlUrl"));
                String readme = escapeSql(rs.getString("readme"));
                Long repositoryId = rs.getObject("repositoryId", Long.class);

                // 构建值字符串
                String valueStr = String.format("(%d, '%s', '%s', %s, %s)",
                        id,
                        fullName,
                        htmlUrl,
                        (readme != null ? "'" + readme + "'" : "NULL"),
                        (repositoryId != null ? repositoryId.toString() : "NULL"));

                batchValues.add(valueStr);
                batchCount++;

                // 每BATCH_SIZE条生成一个批量插入语句
                if (batchCount >= BATCH_SIZE) {
                    writeBatchInsertToFile(fileWriter, batchValues);
                    totalProcessed += batchCount;
                    batchValues.clear();
                    batchCount = 0;
                }
            }

            // 处理剩余的记录
            if (!batchValues.isEmpty()) {
                writeBatchInsertToFile(fileWriter, batchValues);
                totalProcessed += batchValues.size();
            }

            // 写入文件尾
//            fileWriter.write("\nCOMMIT;\n");
//            fileWriter.write("SET FOREIGN_KEY_CHECKS = 1;\n");
//            fileWriter.write("SET UNIQUE_CHECKS = 1;\n");
//            fileWriter.write("SET AUTOCOMMIT = 1;\n\n");
//            fileWriter.write("-- 总共处理记录数: " + totalProcessed + "\n");

            System.out.println("处理完成! 共处理 " + totalProcessed + " 条记录");
            System.out.println("SQL文件已保存到: " + OUTPUT_FILE);

        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
                if (fileWriter != null) fileWriter.close();
            } catch (SQLException | IOException se) {
                se.printStackTrace();
            }
        }
    }

    private static void writeBatchInsertToFile(FileWriter fileWriter, List<String> values) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `repository` (`id`, `fullName`, `htmlUrl`, `readme`, `repositoryId`) VALUES \n");

        for (int i = 0; i < values.size(); i++) {
            sb.append(values.get(i));
            if (i < values.size() - 1) {
                sb.append(",\n");
            }
        }

        sb.append(";\n\n");
        fileWriter.write(sb.toString());
    }

    private static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("'", "''").replace("\\", "\\\\");
    }
}