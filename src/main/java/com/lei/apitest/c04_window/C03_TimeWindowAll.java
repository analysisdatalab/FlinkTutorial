package com.lei.apitest.c04_window;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * @Author: Lei
 * @E-mail: 843291011@qq.com
 * @Date: Created in 3:18 下午 2020/6/7
 * @Version: 1.0
 * @Modified By:
 * @Description:
 */
// timeWindowAll 全局 每N间隔将数据收集成一个组
public class C03_TimeWindowAll {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> lines = env.socketTextStream("localhost", 7777);

        SingleOutputStreamOperator<Integer> nums = lines.map(new MapFunction<String, Integer>() {
            @Override
            public Integer map(String value) throws Exception {
                return Integer.parseInt(value);
            }
        });

        // 不分组，将整体当成一个组
        // 每5秒钟将数据收集成一个组
        AllWindowedStream<Integer, TimeWindow> window = nums.timeWindowAll(Time.seconds(5));

        // 在窗口中聚合
        SingleOutputStreamOperator<Integer> summed = window.sum(0);

        summed.print();

        env.execute("C03_TumblingWindowAll");
    }
}
