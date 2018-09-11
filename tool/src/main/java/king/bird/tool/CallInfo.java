package king.bird.tool;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 18141924293@163.com
 *     time   : 2018/09/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CallInfo {

    public String number; // 号码
    public long date;     // 日期
    public int type;      // 类型：来电、去电、未接

    public CallInfo(String number, long date, int type) {
        this.number = number;
        this.date = date;
        this.type = type;
    }

    @Override
    public String toString() {
        return "CallInfo{" +
                "number='" + number + '\'' +
                ", date=" + date +
                ", type=" + type +
                '}';
    }
}
