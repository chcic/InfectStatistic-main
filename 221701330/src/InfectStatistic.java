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

    public static void InformationProcessing(String Path,List<Province> proList) //日志信息处理
    {
        String pattern = ".*//.*";
        String provincepattern = ".*?\\s";
        String pattern0 = "(\\d+)";
        String pattern0_1 = "感染+";
        String pattern0_2 = "疑似+";
        String pattern1 = ".*新增.*";
        String pattern2 = ".*流入.*";
        String pattern3 = ".*死亡.*";
        String pattern4 = ".*治愈.*";
        String pattern5 = ".*确诊感染.*";
        String pattern6 = ".*排除.*";
        Pattern number = Pattern.compile(pattern0); // 创建 Pattern 对象
        Pattern pro1 = Pattern.compile(provincepattern);
        Pattern pro2 = Pattern.compile(provincepattern);
        try
        {
            File file = new File(Path);
            if(!file .exists())
            {
                System.out.println("文件不存在");
                //System.exit(0);
            }
            else
            {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String str = null;
                while ((str = br.readLine()) != null) {
                    if(Pattern.matches(pattern,str)) continue;
                    // 现在创建 matcher 对象
                    Matcher Pro1 = pro1.matcher(str);
                    Matcher m = number.matcher(str);
                    if (Pro1.find())
                    {
                        System.out.println("Found value: " + Pro1.group(0) );
                    }
                    if(Pattern.matches(pattern1,str))
                    {
                        if (m.find())
                        {
                            System.out.println("Found value: " + m.group(0) );
                        }
                    }
                    else if(Pattern.matches(pattern2,str)) {}
                    else if(Pattern.matches(pattern3,str)) {}
                    else if(Pattern.matches(pattern4,str)) {}
                    else if(Pattern.matches(pattern5,str)) {}
                    else if(Pattern.matches(pattern6,str)) {}
                    System.out.println("读取的内容为：" + str);
                }
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
    public static void getAllFileName(String path,ArrayList<String> listFileName){
        File file = new File(path);
        File [] files = file.listFiles();
        String [] names = file.list();
        if(names != null){
            String [] completNames = new String[names.length];
            for(int i=0;i<names.length;i++){
                completNames[i]=path+names[i];
            }
            listFileName.addAll(Arrays.asList(completNames));
        }
    }
    public static void main(String[] args)
    {
        String log = null,date = null,out = null;
        int listjudge = 0;
        List<Province> proList = new ArrayList<>();
        ArrayList<String> listFileName = new ArrayList<String>();
        for(int j = 0;j<args.length;j++) {
            if(args[j].equals("list")) listjudge++;
            else if(args[j].equals("-log"))
            {
                log = args[j+1];
                listjudge++;
            }
            else if(args[j].equals("-out"))
            {
                out = args[j+1];
                listjudge++;
            }
            else if(args[j].equals("-date")) date = args[j+1];
        }
        if(listjudge!=3)
        {
            System.out.println("命令错误");
            //System.exit(0);
        }
        getAllFileName(log,listFileName);
        for(String name:listFileName){
            if(name.contains(".txt")||name.contains(".properties")){
                InformationProcessing(name,proList);
                if(date==null) continue;
                else if(name.equals(log+date+".log.txt")) break;
            }
        }
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
    }
}
