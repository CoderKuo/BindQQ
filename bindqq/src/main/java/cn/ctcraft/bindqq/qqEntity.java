package cn.ctcraft.bindqq;

public class qqEntity {
    private Long qqId;
    private String name;

    public qqEntity(Long qqId, String name) {
        this.qqId = qqId;
        this.name = name;
    }

    public Long getQqId() {
        return qqId;
    }

    public void setQqId(Long qqId) {
        this.qqId = qqId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "qqEntity{" +
                "qqId=" + qqId +
                ", name='" + name + '\'' +
                '}';
    }
}
