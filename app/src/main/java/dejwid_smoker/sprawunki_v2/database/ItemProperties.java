package dejwid_smoker.sprawunki_v2.database;

/**
 * Created by Dawid on 2016-10-17.
 */

public class ItemProperties {

    private int itemChecked;
    private double itemPrice;
    private double itemCount;
    private String itemUnit;
    private String itemComment;

    public ItemProperties(double itemPrice, double itemCount, String itemUnit, String itemComment) {
        this.itemPrice = itemPrice;
        this.itemCount = itemCount;
        this.itemUnit = itemUnit;
        this.itemComment = itemComment;
    }

    public ItemProperties(int itemChecked, double itemPrice, double itemCount, String itemUnit,
                          String itemComment) {
        this.itemChecked = itemChecked;
        this.itemPrice = itemPrice;
        this.itemCount = itemCount;
        this.itemUnit = itemUnit;
        this.itemComment = itemComment;
    }

    public void setItemChecked(int itemChecked) {
        this.itemChecked = itemChecked;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setItemCount(double itemCount) {
        this.itemCount = itemCount;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public void setItemComment(String itemComment) {
        this.itemComment = itemComment;
    }

    public int getItemChecked() {
        return itemChecked;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public double getItemCount() {
        return itemCount;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public String getItemComment() {
        return itemComment;
    }
}
