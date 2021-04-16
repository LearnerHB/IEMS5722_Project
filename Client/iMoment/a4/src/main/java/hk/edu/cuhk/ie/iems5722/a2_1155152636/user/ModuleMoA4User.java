package hk.edu.cuhk.ie.iems5722.a2_1155152636.user;

public class ModuleMoA4User {

    private String name;
    private String id;

    private ModuleMoA4User() {
    }

    private static volatile ModuleMoA4User mInstnce;

    public static ModuleMoA4User getInstance() throws Exception {
        if (mInstnce == null) {
            synchronized (ModuleMoA4User.class) {
                if (mInstnce == null)
                    mInstnce = new ModuleMoA4User();
                else throw new Exception("获取用户信息失败");
            }
        }
        return mInstnce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setId(int id) {
        this.id = String.valueOf(id);
    }

}
