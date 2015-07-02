package com.dfjy.seal.ksoap;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Project��TestKsoap
 * User: dongxf(dongxf@orient-it.com)
 * Date: 2015-06-24
 * Time: 13:33
 */
public class ServiceDataConver {

    /**处理获得到的服务端数据
     * @param descList  标题信息
     * @param rowsList  行信息
     * @return List<HashMap<String, String>>  表信息
     */
    public static List<HashMap<String, String>> rowsDataConver(ArrayList<String> descList, ArrayList<String> rowsList) {
        int countDesc = descList.size();
        int conntRow = rowsList.size();
        List<HashMap<String, String>> rowsMapList = new LinkedList<HashMap<String, String>>();
        for (int j = 0; j < conntRow; j++) {
            HashMap<String, String> rowsMap = new HashMap<String, String>();
            String rowStr = rowsList.get(j);
            String[] cols;
            cols = rowStr.substring(8, rowStr.length() - 2).split(";");
            for (int i = 0; i < countDesc; i++) {
                String descStr = descList.get(i);
                String[] descs;
                descs = descStr.substring(15, descStr.length() - 2).split(";");
                String descValue = descs[0].trim();
                String itype = descs[1].trim().substring(6);
                String colsValue = cols[i].trim().substring(5);
                byte[] retByte = org.kobjects.base64.Base64.decode(colsValue);
                String colsValueStr = "";
                int colValueInt;
                if (itype.equals("1")) {
                    colValueInt = bytesToInt(retByte, 0);
                    colsValueStr = String.valueOf(colValueInt);
                } else {
                    colsValueStr = changeCharset(retByte, "GBK");
                }
                rowsMap.put(descValue, colsValueStr);
            }
            rowsMapList.add(rowsMap);

        }

        return rowsMapList;

    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }


    /**
     * byte[] 转编码
     *
     * @param bs
     * @param newCharset 编码
     * @return
     */
    public static String changeCharset(byte[] bs, String newCharset) {
        String tempStr = "";
        try {
            tempStr = new String(bs, newCharset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return tempStr;
    }

}