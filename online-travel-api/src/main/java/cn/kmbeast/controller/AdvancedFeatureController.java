package cn.kmbeast.controller;


import cn.kmbeast.service.impl.AdvancedFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/advanced") // 前端访问路径前缀
public class AdvancedFeatureController {

    @Autowired
    private AdvancedFeatureService advancedService;

    // 这是一个统一的简易返回对象，你可以替换成你项目里现有的 Result 类
    private Map<String, Object> createResponse(int code, String msg, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", data);
        return map;
    }

    /**
     * Frontend VIP check API endpoint
     */
    @PostMapping("/checkVip")
    public Map<String, Object> checkVip(@RequestBody Map<String, Integer> params) {
        Integer userId = params.get("userId");
        if (userId == null) {
            return createResponse(400, "User ID cannot be empty", null);
        }

        // Invoke JDBC transaction logic in Service layer
        Map<String, Object> result = advancedService.checkAndUpgradeVip(userId);

        return createResponse(200, "Operation completed", result);
    }

// Additional code for AdvancedFeatureController.java

    @PostMapping("/calculateRevenue")
    public Map<String, Object> calculateRevenue(@RequestBody Map<String, String> params) {
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");

        // Invoke stored procedure logic
        BigDecimal revenue = advancedService.callStoredProcedure(startDate, endDate);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "Stored procedure executed successfully");
        result.put("revenue", revenue);
        return result;
    }

    @PostMapping("/supplierReport")
    public Map<String, Object> getSupplierReport(@RequestBody Map<String, String> params) {
        String startDate = params.get("startDate"); // 格式 "2023-01-01 00:00:00"
        String endDate = params.get("endDate");

        List<Map<String, Object>> data = advancedService.generateSupplierReport(startDate, endDate);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("msg", "Report Generated Successfully");
        response.put("data", data);
        return response;
    }
}