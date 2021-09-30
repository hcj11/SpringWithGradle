package jvm.gc;

import lombok.Data;

import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GreenHouseScheduler {
    private volatile boolean light = false;
    private volatile boolean water = false;
    private String thermostat = "Day";

    public synchronized String getThermostat() {
        return thermostat;
    }

    public synchronized void setThermostat(String thermostat) {
        this.thermostat = thermostat;
    }

    ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(10);

    public void schedule(Runnable event, long delay) {
        scheduler.schedule(event, delay, TimeUnit.MILLISECONDS);
    }

    public void repeat(Runnable event, long delay, long period) {
        scheduler.scheduleWithFixedDelay(event, delay, period, TimeUnit.MILLISECONDS);
    }

    class LightOn implements Runnable {
        @Override
        public void run() {
            // 硬件打开灯
            System.out.println("Turning on the lights");
            light = true;
        }
    }

    class LightOff implements Runnable {
        @Override
        public void run() {
            // 硬件关闭灯
            System.out.println("Turning off the lights");
            light = false;
        }
    }

    class WaterOff implements Runnable {
        @Override
        public void run() {
            // 硬件关闭水
            System.out.println("Turning off the water");
            water = false;
        }
    }

    class WaterOn implements Runnable {
        @Override
        public void run() {
            // 硬件打开水
            System.out.println("Turning on the water");
            water = true;
        }
    }

    class ThermostatNight implements Runnable {
        @Override
        public void run() {
            // 夜晚
            System.out.println("Thermostat to night setting ");
            setThermostat("Night");
        }
    }

    class ThermostatDay implements Runnable {
        @Override
        public void run() {
            // 白天
            System.out.println("Thermostat to day setting ");
            setThermostat("Day");
        }
    }

    class Bell implements Runnable {
        @Override
        public void run() {
            System.out.println("Bing");
        }
    }

    class Teminate implements Runnable {
        @Override
        public void run() {
            System.out.println("Teminating");
            scheduler.shutdownNow();
            // 关闭调度任务，重新开启线程
            new Thread(() -> {
                dataPoints.stream().forEach(d->{System.out.println(d);});
            }).start();
        }
    }

    @Data
    public static class DataPoint {
        final Calendar time;
        final float temporature;
        final float humidity;

        public DataPoint(Calendar clone, float lastTemp, float lastHumidity) {
            this.time=clone;
            this.temporature=lastTemp;
            this.humidity=lastHumidity;
        }

        @Override
        public String toString() {
            return time.getTime() + String.format(" temporature: %1$.1f humidity: %2$.2f", temporature, humidity);
        }
    }

    private Calendar lastTime = Calendar.getInstance();

    {
        lastTime.add(Calendar.MINUTE, 30);
        lastTime.add(Calendar.SECOND, 00);

    }

    ;
    private float lastTemp = 65.0f;
    private int tempDirection = +1;
    private float lastHumidity = 50.0f;
    private int humidityDirection = +1;
    private Random random = new Random(47);
    List<DataPoint> dataPoints = Collections.synchronizedList(new ArrayList<DataPoint>());

    class CollectData implements Runnable {
        @Override
        public void run() {
            System.out.println("Collecting datda");
            synchronized (GreenHouseScheduler.this) {
                // 假装时间间隔比它长
                lastTime.set(Calendar.MINUTE, lastTime.get(Calendar.MINUTE) + 30);
                // 1/5 的机会转变方向
                if (random.nextInt(5) == 4)
                    tempDirection = -tempDirection;
                lastTemp = lastTemp + tempDirection * (1.0f + random.nextFloat());
                if (random.nextInt(5) == 4)
                    humidityDirection = -humidityDirection;
                lastHumidity = lastHumidity + humidityDirection * random.nextFloat();
                // 日历可复制 否则所有的数据点保持上一个时间段的属性，对于类似日历这样的对象，clone()就可以了
                dataPoints.add(new DataPoint((Calendar) lastTime.clone(), lastTemp, lastHumidity));


            }
        }
    }
    /**
     * gc properties
     -Xloggc:gc.log
     -ea -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseG1GC -XX:+PrintGCApplicationStoppedTime -XX:ConcGCThreads=2
     -XX:G1HeapRegionSize=2M -XX:G1HeapWastePercent=5 -XX:G1MixedGCCountTarget=80
     -XX:+UnlockDiagnosticVMOptions -XX:+G1PrintRegionLivenessInfo -XX:G1ReservePercent=10
     -XX:+G1SummarizeRSetStats -XX:+G1TraceConcRefinement -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation
     -XX:+G1UseAdaptiveConcRefinement -XX:GCTimeRatio=9 -XX:+HeapDumpBeforeFullGC -XX:+HeapDumpAfterFullGC
     -XX:InitiatingHeapOccupancyPercent=5 -XX:+UseStringDeduplication -XX:StringDeduplicationAgeThreshold=3
     -XX:+PrintStringDeduplicationStatistics -XX:MaxGCPauseMillis=10 -XX:MinHeapFreeRatio=70 -XX:MaxHeapFreeRatio=99
     -XX:+PrintAdaptiveSizePolicy -XX:-ResizePLAB -XX:+ResizeTLAB -XX:+ClassUnloadingWithConcurrentMark
     -XX:+ClassUnloading -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+UnlockCommercialFeatures
     */
    public static void main(String[] args) {
        GreenHouseScheduler gh = new GreenHouseScheduler();
        gh.schedule(gh.new Teminate(),5000);
        gh.repeat(gh.new Bell(),0,1000);
        gh.repeat(gh.new ThermostatNight(),0,2000);
        gh.repeat(gh.new LightOn(),0,200);
        gh.repeat(gh.new LightOff(),0,400);
        gh.repeat(gh.new WaterOn(),0,600);
        gh.repeat(gh.new WaterOff(),0,800);
        gh.repeat(gh.new ThermostatDay(),0,1400);
        gh.repeat(gh.new CollectData(),500,500);

    }
}
