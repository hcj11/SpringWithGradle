package Utils;

public interface Constants {
    String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    enum ScheduleStatus {
        PAUSE(0), NORMAL(1);
        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
