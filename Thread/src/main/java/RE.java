public class RE {

    private int code;

    private Data data;

    private String msg;

    public RE(int code, Data data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public RE(){

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
