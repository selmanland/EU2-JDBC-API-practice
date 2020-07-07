package jdbctests;

import org.testng.annotations.Test;
import utilities.DBUtils;

import java.util.List;
import java.util.Map;

public class dbutils_practice {


    @Test
    public void test1(){

        //create connection
        DBUtils.createConnection();

        List<Map<String, Object>> queryData = DBUtils.getQueryResultMap("select * from departments");

        for (Map<String, Object> row : queryData) {
            System.out.println(row);
        }

        //close connection
        DBUtils.destroy();
    }

    @Test
    public void test2(){

        DBUtils.createConnection();

        Map<String, Object> rowMap = DBUtils.getRowMap("select * from employees where employee_id =100");

        System.out.println(rowMap.toString());

        DBUtils.destroy();


    }
}
