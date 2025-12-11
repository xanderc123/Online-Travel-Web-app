<template>
  <div class="line-main">
    <div>
      <span class="tag">{{ tag }}</span>
      <span class="time-show">
        <span class="top-bar" style="font-size: 12px;">Time Selection</span>
        <el-select size="mini" style="width: 90px;" v-model="selectedValue" placeholder="Period">
          <el-option v-for="item in options" :key="item.num" :label="item.name" :value="item.num">
          </el-option>
        </el-select>
      </span>
    </div>
    <div ref="chart" :style="{ width: '100%', height: height }"></div>
  </div>
</template>
<script>
// 折线图组件
import * as echarts from 'echarts';
export default {
  name: 'LineChart',
  props: {
    tag: {
      type: String,
      default: 'Line Chart'
    },
    values: {
      type: Array,
      required: true
    },
    date: {
      type: Array,
      required: true
    },
    height: {
      type: String,
      default: '220px'
    },
  },
  watch: {
    selectedValue(v1, v2) {
      this.$emit('on-selected', v1);
    },
    values(v1,v2){
      this.initChart();
    }
  },
  data() {
    return {
      chart: null,
      options: [{ num: 7, name: 'Last 7 days' }, { num: 30, name: 'Last 30 days' }, { num: 60, name: 'Last 60 days' }],
      selectedValue: '',
    }
  },
  methods: {
    // Initialize chart
    initChart() {
      this.chart = echarts.init(this.$refs.chart);
      let option = {
        grid: {
          left: 30,
          right: 10,
          top: 50,
          borderWidth: 0,
        },
        title: {
          text: '',
          color: '#ffffff',
        },
        tooltip: {
          trigger: 'axis',
          formatter: 'On day {b}, total transaction amount {c} dollar',
        },
        legend: {
          data: ['']
        },
        xAxis: {
          data: this.date,
          axisLine: { show: false },
          axisTick: { show: false },
          axisLabel: {
            color: 'rgb(102, 102, 102)',
            fontSize: '12'
          },
        },
        yAxis: {
          axisLine: { show: false },
          axisTick: { show: false },
          axisLabel: {
            color: 'rgb(102, 102, 102)',
            fontSize: '12'
          },
        },
        series: [{
          name: '',
          type: 'line',
          smooth: true,
          data: this.values,
          areaStyle: {
            color: 'rgba(135, 115, 232, 0.3)'
          },
          lineStyle: {
            color: '#5B8FF9'
          },
          itemStyle: {
            color: '#5B8FF9',
            borderColor: '#5B8FF9',
            borderWidth: 2
          },
          label: {
            show: true,
            position: 'top',
            color: 'rgb(102, 102, 102)',
          },
        }]
      };
      this.chart.setOption(option);
    },
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
  },
};  
</script>
<style scoped lang="scss">
.line-main {
  margin-bottom: 5px;
  border-radius: 3px;

  .tag {
    font-size: 16px;
    padding: 15px 6px;
    display: inline-block;
    color: #333;
    font-weight: bold;
  }

  .time-show {
    padding: 15px 6px;
    float: right;
  }

}
</style>