<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>🏆 Supplier Performance Evaluation (Advanced DB Demo)</span>
    </div>
    
    <div style="margin-bottom: 20px;">
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="To"
        start-placeholder="Start Date"
        end-placeholder="End Date"
        value-format="yyyy-MM-dd HH:mm:ss">
      </el-date-picker>
      <el-button type="primary" @click="generateReport" :loading="loading" style="margin-left: 10px;">
        Generate Performance Report
      </el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" stripe border>
      <el-table-column prop="supplierName" label="Supplier Name"></el-table-column>
      <el-table-column prop="totalSales" label="Total Sales (¥)" sortable>
         <template slot-scope="scope">
            {{ scope.row.totalSales || 0 }}
         </template>
      </el-table-column>
      <el-table-column prop="avgRating" label="Average Rating" sortable></el-table-column>
      <el-table-column prop="level" label="Performance Level">
        <template slot-scope="scope">
          <el-tag :type="getLevelColor(scope.row.level)">{{ scope.row.level }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
export default {
  data() {
    return {
      dateRange: ['2023-01-01 00:00:00', '2025-12-31 23:59:59'], // Default range
      tableData: [],
      loading: false
    };
  },
  methods: {
    getLevelColor(level) {
      if (level === 'Premium') return 'success';
      if (level === 'Standard') return 'primary';
      return 'info';
    },
    async generateReport() {
      if (!this.dateRange) {
        this.$message.warning("Please select a date range");
        return;
      }
      this.loading = true;
      try {
        const res = await this.$axios.post('/advanced/supplierReport', {
          startDate: this.dateRange[0],
          endDate: this.dateRange[1]
        });
        
        if (res.data.code === 200) {
          this.tableData = res.data.data;
          this.$message.success("Report generated successfully! Trigger has logged the operation.");
        } else {
          this.$message.error("Failed to generate report");
        }
      } catch (e) {
        console.error(e);
        this.$message.error("System error");
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>