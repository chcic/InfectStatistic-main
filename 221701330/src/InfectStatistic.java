/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
import java.util.*;
import java.text.Collator;
import java.util.regex.*;
import  java.io.*;
import  java.lang.*;
class Province implements Comparable<Province>
{
    String province=null;
    int ip = 0,sp = 0,cure = 0,dead = 0;
    public Province(String province,int ip,int sp,int cure,int dead)
    {
        this.province = province;
        this.ip = ip;
        this.sp = sp;
        this.cure = cure;
        this.dead = dead;
    }

    public Province() {}

    public int compareTo(Province a)
    {
        String province1 = this.province;
        String province2 = a.province;
        Collator instance = Collator.getInstance(Locale.CHINA);
        if(province1.indexOf("重") >= 0){
            province1 = province1.replaceAll("重", "冲");//多音字重chong庆
        }
        if(province2.indexOf("重") >= 0){
            province2 = province2.replaceAll("重", "冲");
        }
        return instance.compare(province1,province2);  //按拼音排序
    }

    public void printmessage()
    {
        System.out.println("province:"+province+" ip:"+ip+" sp:"+sp+" cure:"+cure+" dead:"+dead);
    }
}

class InfectStatistic {
    public static Boolean wronglist1(String args[])
    {
        boolean wrong = false;
        return wrong;
    }

    public static void log(String Path)
    {
        try
        {
            File file = new File(Path);
            if(!file .exists()) {
                System.out.println("文件不存在");
                //System.exit(0);
            }
            else{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String str = null;
                while ((str = br.readLine()) != null) System.out.println("读取的内容为：" + str);
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        String pattern1 = "list";
        String pattern2 = "-log";
        String pattern3 = "-out";
        String pattern4 = "-date";
        String pattern5 = "-type";
        String Path = null;
        int i = 0;
        boolean isMatch1 = false,isMatch2 = false,isMatch3 = false,isMatch4 = false,isMatch5 = false;
        for(i = 0;i<args.length;i++) {
            isMatch1 = Pattern.matches(pattern1,args[i]);
            if(isMatch1) break;
        }
        for(i = 0;i<args.length;i++) {
            isMatch2 = Pattern.matches(pattern2,args[i]);
            if(isMatch2) {
                Path = args[i+1];
                break;
            }
        }
        for(i = 0;i<args.length;i++) {
            isMatch3 = Pattern.matches(pattern3,args[i]);
            if(isMatch3) break;
        }
        for(i = 0;i<args.length;i++) {
            isMatch4 = Pattern.matches(pattern4,args[i]);
            if(isMatch4) {
                Path+=args[i+1];
                System.out.println(Path);
                break;
            }
        }
        if(isMatch2) log(Path+".log.txt");
        for(i = 0;i<args.length;i++) {
            isMatch5 = Pattern.matches(pattern5,args[i]);
            if(isMatch5) break;
        }
        boolean listjudge = isMatch1&&isMatch2&&isMatch3;;
        if(!listjudge)
        {
            System.out.println("命令错误");
            //System.exit(0);
        }
        List<Province> proList = new ArrayList<>();
        Province pro1 = new Province("北京",3,4,5,6);
        Province pro2 = new Province("重庆",5,5,5,5);
        Province pro3 = new Province("湖北",1,2,3,4);
        proList.add(pro1);
        proList.add(pro2);
        proList.add(pro3);
        Collections.sort(proList);
        for (Province province : proList) {
            province.printmessage();
        }
        System.out.println(listjudge);
    }
}
