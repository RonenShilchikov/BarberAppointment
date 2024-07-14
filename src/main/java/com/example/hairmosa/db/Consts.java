package com.example.hairmosa.db;

import java.util.Arrays;
import java.util.List;

public class Consts {
    public static String USERS_DB = "users";
    public static String EMPLOYESS_DB = "employees";
    public static String APPOINTEMNTS_DB = "appointments";
    public static String PRODUCTS_DB = "products";
    public static String BRANCHES_DB = "branches";

    public static String DATE_COLUMN = "date";
    public static String ID_OF_WORKER_COLUMN = "idOfWorker";
    public static String BRANCH_ID_COLUMN = "branchId";

    public static String CLIENT_UID_COLUMN = "clientUID";

    public static final List<String> AVAILABLE_HOURS = Arrays.asList(
            "10:00",
            "10:20",
            "10:40",
            "11:00",
            "11:20",
            "11:40",
            "12:00",
            "12:20",
            "12:40",
            "13:00",
            "13:20",
            "13:40",
            "14:00",
            "14:20",
            "14:40",
            "15:00",
            "15:20",
            "15:40",
            "16:00",
            "16:20",
            "16:40",
            "17:00",
            "17:20",
            "17:40",
            "18:00",
            "18:20",
            "18:40",
            "19:00"
    );
}
