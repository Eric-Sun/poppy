package com.j13.poppy;

public class SystemErrorCode {

    public static class Common {
        public static int INPUT_PARAMETER_ERROR = 14;
        public static int NEED_T = 15;
        public static int T_EXPIRE=16;
    }

    public static class System {
        public static int SYSTEM_ERROR = 1;
        public static int NOT_FOUND_ACTION = 2;
        public static int ACTION_REFLECT_ERROR = 3;
        public static int REFLECT_ERROR = 4;
        public static int PARSE_REQUEST_POST_DATA_ERROR = 5;
    }


}
