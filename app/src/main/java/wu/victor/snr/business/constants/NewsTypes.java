package wu.victor.snr.business.constants;

/**
 * Created by wuguonan on 2017/3/14 0014.
 */

public class NewsTypes {

  public enum Types {
    TOP("top", "头条"),
    SHEHUI("shehui", "社会"),
    GUOJI("guoji", "国际"),
    GUONEI("guonei", "国内"),
    YULE("yule", "娱乐"),
    TIYU("tiyu", "体育"),
    JUNSHI("junshi", "军事"),
    KEJI("keji", "科技"),
    CAIJING("caijing", "财经"),
    SHISHANG("shishang", "时尚");

    String name;

    String queryParam;

    Types(String queryParam, String name) {
      this.name = name;
      this.queryParam = queryParam;
    }

    public String getName() {
      return name;
    }

    public String getQueryParam() {
      return queryParam;
    }
  }
}
