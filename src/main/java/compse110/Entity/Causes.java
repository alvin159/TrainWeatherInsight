package compse110.Entity;

// IMPORTANT! Gson library is required to run this code. DONT modify this class
public class Causes {

    private String categoryCode;
    private String detailedCategoryCode;
    private String thirdCategoryCode;
    private long categoryCodeId;
    private long detailedCategoryCodeId;
    private long thirdCategoryCodeId;

    public Causes() {
    }

    public Causes(String categoryCode, String detailedCategoryCode, String thirdCategoryCode, long categoryCodeId, long detailedCategoryCodeId, long thirdCategoryCodeId) {
        this.categoryCode = categoryCode;
        this.detailedCategoryCode = detailedCategoryCode;
        this.thirdCategoryCode = thirdCategoryCode;
        this.categoryCodeId = categoryCodeId;
        this.detailedCategoryCodeId = detailedCategoryCodeId;
        this.thirdCategoryCodeId = thirdCategoryCodeId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getDetailedCategoryCode() {
        return detailedCategoryCode;
    }

    public void setDetailedCategoryCode(String detailedCategoryCode) {
        this.detailedCategoryCode = detailedCategoryCode;
    }

    public String getThirdCategoryCode() {
        return thirdCategoryCode;
    }

    public void setThirdCategoryCode(String thirdCategoryCode) {
        this.thirdCategoryCode = thirdCategoryCode;
    }

    public long getCategoryCodeId() {
        return categoryCodeId;
    }

    public void setCategoryCodeId(long categoryCodeId) {
        this.categoryCodeId = categoryCodeId;
    }

    public long getDetailedCategoryCodeId() {
        return detailedCategoryCodeId;
    }

    public void setDetailedCategoryCodeId(long detailedCategoryCodeId) {
        this.detailedCategoryCodeId = detailedCategoryCodeId;
    }

    public long getThirdCategoryCodeId() {
        return thirdCategoryCodeId;
    }

    public void setThirdCategoryCodeId(long thirdCategoryCodeId) {
        this.thirdCategoryCodeId = thirdCategoryCodeId;
    }

    @Override
    public String toString() {
        return "Causes{" +
                "categoryCode='" + categoryCode + '\'' +
                ", detailedCategoryCode='" + detailedCategoryCode + '\'' +
                ", thirdCategoryCode='" + thirdCategoryCode + '\'' +
                ", categoryCodeId=" + categoryCodeId +
                ", detailedCategoryCodeId=" + detailedCategoryCodeId +
                ", thirdCategoryCodeId=" + thirdCategoryCodeId +
                '}';
    }
}
