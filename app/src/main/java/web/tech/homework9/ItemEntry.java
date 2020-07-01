package web.tech.homework9;

import android.media.Image;

public class ItemEntry {
    private String title;
    private String price;
    private String imageUrl;
    private String shipping;
    private String condition;
    private boolean isTopRated;
    private String id;
    private String itemUrl;
    private String oneDayShip;
    private String expeditedShip;
    private String shipType;
    private String shipsFrom;
    private String shipsTo;

    public ItemEntry(String title, String price, String imageUrl, String shipping, String condition, boolean isTopRated, String id, String itemUrl, String oneDayShip, String expeditedShip, String shipType, String shipsFrom, String shipsTo) {
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.shipping = shipping;
        this.condition = condition;
        this.isTopRated = isTopRated;
        this.id = id;
        this.itemUrl = itemUrl;
        this.oneDayShip = oneDayShip;
        this.expeditedShip = expeditedShip;
        this.shipType = shipType;
        this.shipsFrom = shipsFrom;
        this.shipsTo = shipsTo;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return "$" + price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getShipping() {
        return "$" + shipping;
    }

    public String getCondition() {
        return condition;
    }

    public boolean isTopRated() {
        return isTopRated;
    }

    public String getId() {
        return id;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public String getOneDayShip() {
        return oneDayShip;
    }

    public String getExpeditedShip() {
        return expeditedShip;
    }

    public String getShipType() {
        return shipType;
    }

    public String getShipsFrom() {
        return shipsFrom;
    }

    public String getShipsTo() {
        return shipsTo;
    }
}
