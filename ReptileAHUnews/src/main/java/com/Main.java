package com;

/**
 * @author Jianshu
 * @time 20200823
 */
public class Main {
    public static void main(String args[]){
        /*
        *下面是一些实例化和调用类的一些操作
         */
        String startUrl="http://news.ahu.edu.cn/4642/list.htm";
        GetHrefList GH=new GetHrefList(startUrl);
        HTMLAnalysis HA=new HTMLAnalysis(GH.getAllPageHrefList());
        HA.Analysis();
    }
}
