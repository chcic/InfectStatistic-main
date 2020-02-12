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
    public Province()
    {}
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
    public static void main(String[] args)
    {
        List<Province> proList = new ArrayList<>();
        Province pro1 = new Province("北京",3,4,5,6);
        Province pro2 = new Province("重庆",5,5,5,5);
        Province pro3 = new Province("湖北",1,2,3,4);
        proList.add(pro1);
        proList.add(pro2);
        proList.add(pro3);
        System.out.println("排序前的proList集合：");
        for (Province province : proList) {
            province.printmessage();
        }
        Collections.sort(proList);
        for (Province province : proList) {
            province.printmessage();
        }
    }
}
