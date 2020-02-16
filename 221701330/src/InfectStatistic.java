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
    private String province = null;
    private int ip = 0,sp = 0,cure = 0,dead = 0;
    public Province(String province,int ip,int sp,int cure,int dead)
    {
        this.province = province;
        this.ip = ip;
        this.sp = sp;
        this.cure = cure;
        this.dead = dead;
    }
    public Province(String province)
    {
        this.province = province;
    }
    public void setprovince(String pro)
    {
        this.province = pro;
    }

    public String getprovince()
    {
        return this.province;
    }
    public void setip(int ip)
    {
        this.ip = ip;
    }
    public int getip()
    {
        return this.ip;
    }
    public void setsp(int sp)
    {
        this.sp = sp;
    }
    public int getsp()
    {
        return this.sp;
    }
    public void setcure(int cure)
    {
        this.cure = cure;
    }
    public int getcure()
    {
        return this.cure;
    }
    public void setdead(int dead)
    {
        this.dead = dead;
    }
    public int getdead()
    {
        return this.dead;
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
        if(province1.equals("全国")) return -1; // 全国排第一
        else if(province2.equals("全国")) return 1;
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
    public static void addlist(String provence,List<Province> proList) // 判断列表中有没有该省份，无则加入
    {
        boolean inlist = false;
        for (Province province : proList) {
            inlist = provence.equals(province.getprovince());
            if(inlist) break ;
        }
        if(!inlist) // 若列表里没有该省份，加入列表
        {
            Province pro1 = new Province(provence);
            proList.add(pro1);
        }
    }

    public static void InformationProcessing(String Path,List<Province> proList,Province allpro) //日志信息处理
    {
        int ispro = 0,ip = 0,sp = 0,cure = 0,dead = 0;
        String pattern = ".*//.*";
        String pattern0 = "(\\d+)";
        String pattern0_1 = ".*感染患者.*";
        String pattern0_2 = ".*疑似患者.*";
        String pattern1 = ".*新增.*";
        String pattern2 = ".*流入.*";
        String pattern3 = ".*死亡.*";
        String pattern4 = ".*治愈.*";
        String pattern5 = ".*确诊感染.*";
        String pattern6 = ".*排除.*";
        Pattern number = Pattern.compile(pattern0); // 创建 Pattern 对象
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
                    Matcher num = number.matcher(str); // 现在创建 matcher 对象
                    String arrays[] = str.split(" ");
                    if(Pattern.matches(pattern,str)) continue; // 跳过注释内容
                    addlist(arrays[0],proList);
                    for (Province province : proList) {
                        if (arrays[0].equals(province.getprovince())&&num.find())
                        {
                            if(Pattern.matches(pattern1,str)) // 新增
                            {
                                if(Pattern.matches(pattern0_1,str)) // 新增感染患者
                                {
                                    ip = Integer.valueOf(num.group(0));
                                    province.setip(province.getip()+ip);
                                    allpro.setip(allpro.getip()+ip);
                                }
                                else if(Pattern.matches(pattern0_2,str)) //新增疑似患者
                                {
                                    sp = Integer.valueOf(num.group(0));
                                    province.setsp(province.getsp()+sp);
                                    allpro.setsp(allpro.getsp()+sp);
                                }

                            }
                            else if(Pattern.matches(pattern2,str)) //从省一流入省二
                            {
                                addlist(arrays[3],proList); //判断省二有没有在列表里边

                            }
                            else if(Pattern.matches(pattern3,str)) {}
                            else if(Pattern.matches(pattern4,str)) {}
                            else if(Pattern.matches(pattern5,str)) {}
                            else if(Pattern.matches(pattern6,str)) {}
                        }
                    }
                    System.out.println("读取的内容为：" + str);
                    ip = sp =cure = dead =0;
                }
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
    public static void getAllFileName(String path,ArrayList<String> listFileName){
        File file = new File(path);
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
        Province allpro = new Province("全国"); // 创建全国对象
        proList.add(allpro);
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
                InformationProcessing(name,proList,allpro);
                if(date==null) continue;
                else if(name.equals(log+date+".log.txt")) break;
            }
        }
        for (Province province : proList) {
            province.printmessage();
        }
        Collections.sort(proList);
        for (Province province : proList) {
            province.printmessage();
        }
    }
}
