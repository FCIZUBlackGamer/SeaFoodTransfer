package com.ccis.seafoodtrans.APIS;

public class API {
    final public static String BASE_URL = "https://seafoodccis.000webhostapp.com/";

    final public static String REGISTER_URL = BASE_URL + "UserRegister.php";
    final public static String LOGIN_URL = BASE_URL + "Login.php";
    //region Add
    final public static String ADD_PRODUCT_URL = BASE_URL + "Product/Add.php";
    final public static String ADD_COMMENT_URL = BASE_URL + "Comment/Add.php";
    final public static String ADD_TRANSFER_URL = BASE_URL + "Transfer/Add.php";
    final public static String ADD_CONTRACT_URL = BASE_URL + "Contract/Add.php";
    //endregion
    //region List
    final public static String LIST_PRODUCT_URL = BASE_URL + "Product/List.php";
    final public static String LIST_COMMENT_URL = BASE_URL + "Comment/List.php";
    final public static String LIST_TRANSFER_URL = BASE_URL + "Transfer/List.php";
    final public static String LIST_CONTRACT_URL = BASE_URL + "Contract/List.php";
    final public static String LIST_ALL_USERS_CONTRACT_URL = BASE_URL + "Users/ListAll.php";
    final public static String LIST_UserProfile_URL = BASE_URL + "GetUserProfile.php";
    final public static String LIST_CLIENTS_URL = BASE_URL + "Users/ListClient.php";
    final public static String LIST_FISHERMEN_URL = BASE_URL + "Users/ListFisherman.php";
    //endregion
    //region Update
    final public static String UPDATE_PRODUCT_URL = BASE_URL + "Product/Update.php";
    final public static String UPDATE_TRANSFER_URL = BASE_URL + "Transfer/Update.php";
    final public static String UPDATE_CONTRACT_URL = BASE_URL + "Contract/Update.php";
    //endregion
    //region Delete
    final public static String DELETE_PRODUCT_URL = BASE_URL + "Product/Delete.php";
    final public static String DELETE_COMMENT_URL = BASE_URL + "Comment/Delete.php";
    final public static String DELETE_TRANSFER_URL = BASE_URL + "Transfer/Delete.php";
    final public static String DELETE_CONTRACT_URL = BASE_URL + "Contract/Delete.php";
    //endregion

}