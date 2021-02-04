package tools.roc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * File Description/檔案描述: 檢查統一編號
 *
 * @author JamesChang
 * @version 1.0
 * @since 2020/6/27下午 12:49
 **/
public class CheckId {

    public static void main(String[] args) throws Exception {
        System.out.println(validateResident2("A123456789"));
    }

    /*國民身分證代號換算數字表*/
    private static final HashMap<String, Integer> pidResidentFirstMap = new HashMap<String, Integer>(){{
        put("A",10);put("B",11);put("C",12);put("D",13);put("E",14);put("F",15);put("G",16); put("H",17);put("J",18);
        put("K",19);put("L",20);put("M",21);put("N",22);put("P",23);put("Q",24);put("R",25);put("S",26);put("T",27);
        put("U",28);put("V",29);put("X",30);put("Y",31);put("W",32);put("Z",33);put("I",34);put("O",35);
    }};

    /**
     *  檢查居留證(2020新式)
     *  example: CheckId.validateResident2("A900000016")
     * @param inputIdno 檢測字號
     * @return (Boolean) 檢查結果
     */
    public static boolean validateResident2(String inputIdno) throws Exception {
        boolean returnCheck;
        String inputIdnoCheckNumber;

        /*0.檢查輸入碼是否10碼*/
        if(inputIdno.length()!=10){
            return false;
        }
        else if (!inputIdno.matches("[A-Z]{1}[1-2]{1}[0-9]{8}")) {
            return false;
        }
        /*1.取得統號*/
        String idNumber = pidResidentFirstMap.get(inputIdno.substring(0,1))+inputIdno.substring(1,9);
        /*2.取得統一證號檢查碼*/
        inputIdnoCheckNumber = inputIdno.substring(9,10);

        /*3.判斷與輸入的檢查碼是否正確*/
        returnCheck = (getCheckNumber(idNumber) == Integer.parseInt(inputIdnoCheckNumber));
        return returnCheck;
    }

    /**
     * 建立居留證(2020新式)
     *  example: CheckId.creatResident2("A","8","7","000000")
     * @param twnspcode 區域碼(A-Z)
     * @param sex 性別 ：男(8)，女(9)
     * @param idType 人別；外國人(0-6)，無戶籍國民(7)，香港澳門居民(8)，大陸地區人民(9)
     * @param seq；流水號
     * @return
     */
    public static String creatResident2(
            String twnspcode,
            String sex,
            String idType,
            String seq
    ) throws Exception {
        final List englishUpper = Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z");
        final List sexL = Arrays.asList("8","9");
        final List idTypeL = Arrays.asList("0","1","2","3","4","5","6","7","8","9");

        /*資料檢查*/
        //twnspcode
        if(!englishUpper.contains(twnspcode)){
            throw new Exception("the function creatResident2 param[twnspcode] range not in (A ~ Z) ");
        }

        //sex
        if(!sexL.contains(sex)){
            throw new Exception("the function creatResident2 param[sex] range not in (8,9) ");
        }

        //idType
        if(!idTypeL.contains(idType)){
            throw new Exception("the function creatResident2 param[idType] range not in (0 ~ 9) ");
        }

        //seq
        if(seq.length()!=6){
            throw new Exception("the function creatResident2 param[seq] length not equal 6 ");
        }


        String returnValue;
        /*一輸入的資料編製成統號*/
        String idNumber = pidResidentFirstMap.get(twnspcode)+sex+idType+seq;

        returnValue = twnspcode+sex+idType+seq+getCheckNumber(idNumber).toString();

        return returnValue;
    }

    /**
     * 計算出 居留證(2020新式) 檢查碼
     * @param inputIdNumber 統號
     * @return (Integer) 檢查碼
     */
    private static Integer getCheckNumber(String inputIdNumber) throws Exception {
        if(inputIdNumber.length()!=10){
            throw new Exception("the function getCheckNumber param[inputIdNumber] length not equal 10 ");
        }
        /*特定數*/
        final char[] spicalString = {'1','9','8','7','6','5','4','3','2','1'};
        /*從統號計算出檢查碼*/
        Integer returnNumber = 0;
        for(int i = 0; i <inputIdNumber.length(); i++){
            int countNumber =  (Integer.parseInt(String.valueOf(inputIdNumber.charAt(i)))*Integer.parseInt(String.valueOf(spicalString[i])))%10;
            returnNumber +=countNumber;
        }
        /*10 - 計算出檢查碼*/
        returnNumber = 10 - returnNumber%10;
        return returnNumber;
    }

    /**
     * 身分證字號與居留證(統一證)編號檢核程式碼(2020以前)
     * @param str
     * @return 正確 true
     */
    public boolean isValidIDorROCNumber(String str) {

        boolean returnValue = false;

        if (str == null || "".equals(str)) {
            returnValue =  false;
        }

        final char[] pidCharArray = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        // 原身分證英文字應轉換為10~33，這裡直接作個位數*9+10
        final int[] pidIDInt = { 1, 10, 19, 28, 37, 46, 55, 64, 39, 73, 82, 2, 11, 20, 48, 29, 38, 47, 56, 65, 74, 83, 21, 3, 12, 30 };

        // 原居留證第一碼英文字應轉換為10~33，十位數*1，個位數*9，這裡直接作[(十位數*1) mod 10] + [(個位數*9) mod 10]
        final int[] pidResidentFirstInt = { 1, 10, 9, 8, 7, 6, 5, 4, 9, 3, 2, 2, 11, 10, 8, 9, 8, 7, 6, 5, 4, 3, 11, 3, 12, 10 };

        // 原居留證第二碼英文字應轉換為10~33，並僅取個位數*6，這裡直接取[(個位數*6) mod 10]
        final int[] pidResidentSecondInt = {0, 8, 6, 4, 2, 0, 8, 6, 2, 4, 2, 0, 8, 6, 0, 4, 2, 0, 8, 6, 4, 2, 6, 0, 8, 4};

        str = str.toUpperCase();// 轉換大寫
        final char[] strArr = str.toCharArray();// 字串轉成char陣列
        int verifyNum = 0;

        /* 檢查身分證字號 */
        if (str.matches("[A-Z]{1}[1-2]{1}[0-9]{8}")) {
            // 第一碼
            verifyNum = verifyNum + pidIDInt[Arrays.binarySearch(pidCharArray, strArr[0])];
            // 第二~九碼
            for (int i = 1, j = 8; i < 9; i++, j--) {
                verifyNum += Character.digit(strArr[i], 10) * j;
            }
            // 檢查碼
            verifyNum = (10 - (verifyNum % 10)) % 10;

            returnValue = verifyNum == Character.digit(strArr[9], 10);
        }

        /* 檢查統一證(居留證)編號 */
        verifyNum = 0;
        if (str.matches("[A-Z]{1}[A-D]{1}[0-9]{8}")) {
            // 第一碼
            verifyNum += pidResidentFirstInt[Arrays.binarySearch(pidCharArray, strArr[0])];
            // 第二碼
            verifyNum += pidResidentSecondInt[Arrays.binarySearch(pidCharArray, strArr[1])];
            // 第三~八碼
            for (int i = 2, j = 7; i < 9; i++, j--) {
                verifyNum += Character.digit(strArr[i], 10) * j;
            }
            // 檢查碼
            verifyNum = (10 - (verifyNum % 10)) % 10;

            returnValue =  verifyNum == Character.digit(strArr[9], 10);
        }

        return returnValue;
    }
}