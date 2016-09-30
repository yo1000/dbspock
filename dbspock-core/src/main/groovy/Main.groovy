import com.yo1000.dbspock.Tables

/**
 * Created by yo1000 on 2016/09/28.
 */
class Main {
    public static void main(String[] args) {
        def data = {
            table1 {
                c1     | c2     | c3
                'r1c1' | 'r1c2' | 'r1c3'
                'r2c1' | 'r2c2' | 'r2c3'
            }
//
//            table2 {
//                col 'cStr' | 'cInt' | 'cDate'
//                row 'str1' | 100    | '2016-09-26 18:46:01'
//                row 'str2' | 200    | '2016-09-26 18:46:02'
//            }
        }

        data.delegate = new Tables()
        data.call()
    }
}
