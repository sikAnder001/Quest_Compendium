package com.questcompendium.model;

import java.util.ArrayList;

public class Menu {

    private String fbIsSuccess;

    private FoMenuList[] foMenuList;

    public ArrayList<FoHotelList> getFoHotelList() {
        return foHotelList;
    }

    public void setFoHotelList(ArrayList<FoHotelList> foHotelList) {
        this.foHotelList = foHotelList;
    }

    private ArrayList<FoHotelList> foHotelList;

    public String getFbIsSuccess() {
        return fbIsSuccess;
    }

    public void setFbIsSuccess(String fbIsSuccess) {
        this.fbIsSuccess = fbIsSuccess;
    }

    public FoMenuList[] getFoMenuList() {
        return foMenuList;
    }

    public void setFoMenuList(FoMenuList[] foMenuList) {
        this.foMenuList = foMenuList;
    }

    @Override
    public String toString() {
        return "ClassPojo [fbIsSuccess = " + fbIsSuccess + ", foMenuList = " + foMenuList + "]";
    }

    public class FoHotelList {
        private String fiHotelsId;

        private String fsHotelName;
        private String fsShortDesc;

        private String fsImage;

        private String fsAppBackgroundImage;
        private String fsAddress;

        private String fiLatitude;
        private String fiLongitude;

        public String getFsShortDesc() {
            return fsShortDesc;
        }

        public void setFsShortDesc(String fsShortDesc) {
            this.fsShortDesc = fsShortDesc;
        }

        public String getFsAddress() {
            return fsAddress;
        }

        public void setFsAddress(String fsAddress) {
            this.fsAddress = fsAddress;
        }

        public String getFiHotelsId() {
            return fiHotelsId;
        }

        public void setFiHotelsId(String fiHotelsId) {
            this.fiHotelsId = fiHotelsId;
        }

        public String getFsHotelName() {
            return fsHotelName;
        }

        public void setFsHotelName(String fsHotelName) {
            this.fsHotelName = fsHotelName;
        }

        public String getFsImage() {
            return fsImage;
        }

        public void setFsImage(String fsImage) {
            this.fsImage = fsImage;
        }

        public String getFiLatitude() {
            return fiLatitude;
        }

        public void setFiLatitude(String fiLatitude) {
            this.fiLatitude = fiLatitude;
        }

        public void setFiLongitude(String fiLongitude) {
            this.fiLongitude = fiLongitude;
        }

        public String getFiLongitude() {
            return fiLongitude;
        }

        public String getFsAppBackgroundImage() {
            return fsAppBackgroundImage;
        }

        public void setFsAppBackgroundImage(String fsAppBackgroundImage) {
            this.fsAppBackgroundImage = fsAppBackgroundImage;
        }
    }

    public class FoMenuList {
        private String fiMenuId;

        private String fsMenuName;

        private String fsImage;

        public String getFiMenuId() {
            return fiMenuId;
        }

        public void setFiMenuId(String fiMenuId) {
            this.fiMenuId = fiMenuId;
        }

        public String getFsMenuName() {
            return fsMenuName;
        }

        public void setFsMenuName(String fsMenuName) {
            this.fsMenuName = fsMenuName;
        }

        public String getFsImage() {
            return fsImage;
        }

        public void setFsImage(String fsImage) {
            this.fsImage = fsImage;
        }

        @Override
        public String toString() {
            return "ClassPojo [fiMenuId = " + fiMenuId + ", fsMenuName = " + fsMenuName + ", fsImage = " + fsImage + "]";
        }
    }
}

