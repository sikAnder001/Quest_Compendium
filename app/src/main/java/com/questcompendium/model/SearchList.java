package com.questcompendium.model;

import java.util.ArrayList;

public class SearchList {
    private ArrayList<FoFacilityList> foFacilityList;

    private String fbIsSuccess;

    private ArrayList<FoCatList> foCatList;

    private ArrayList<FoMenuList> foMenuList;

    public ArrayList<FoMenuList> getFoMenuList() {
        return foMenuList;
    }

    public void setFoMenuList(ArrayList<FoMenuList> foMenuList) {
        this.foMenuList = foMenuList;
    }

    public ArrayList<FoFacilityList> getFoFacilityList() {
        return foFacilityList;
    }

    public void setFoFacilityList(ArrayList<FoFacilityList> foFacilityList) {
        this.foFacilityList = foFacilityList;
    }

    public String getFbIsSuccess() {
        return fbIsSuccess;
    }

    public void setFbIsSuccess(String fbIsSuccess) {
        this.fbIsSuccess = fbIsSuccess;
    }

    public ArrayList<FoCatList> getFoCatList() {
        return foCatList;
    }

    public void setFoCatList(ArrayList<FoCatList> foCatList) {
        this.foCatList = foCatList;
    }

    @Override
    public String toString() {
        return "ClassPojo [foFacilityList = " + foFacilityList + ", fbIsSuccess = " + fbIsSuccess + ", foCatList = " + foCatList + "]";
    }


    public class FoFacilityList {
        private String fsFacilityBanner;

        private String fsFacilityName;

        private String fiFacilityId;

        private String fsFacilityicon;
        private String fsFacilityMenuId;

        private String fsDescriptionLink;

        public String getFsFacilityBanner() {
            return fsFacilityBanner;
        }

        public void setFsFacilityBanner(String fsFacilityBanner) {
            this.fsFacilityBanner = fsFacilityBanner;
        }

        public String getFsFacilityName() {
            return fsFacilityName;
        }

        public String getFsFacilityMenuId() {
            return fsFacilityMenuId;
        }

        public void setFsFacilityMenuId(String fsFacilityMenuId) {
            this.fsFacilityMenuId = fsFacilityMenuId;
        }

        public void setFsFacilityName(String fsFacilityName) {
            this.fsFacilityName = fsFacilityName;
        }

        public String getFiFacilityId() {
            return fiFacilityId;
        }

        public void setFiFacilityId(String fiFacilityId) {
            this.fiFacilityId = fiFacilityId;
        }

        public String getFsFacilityicon() {
            return fsFacilityicon;
        }

        public void setFsFacilityicon(String fsFacilityicon) {
            this.fsFacilityicon = fsFacilityicon;
        }

        public String getFsDescriptionLink() {
            return fsDescriptionLink;
        }

        public void setFsDescriptionLink(String fsDescriptionLink) {
            this.fsDescriptionLink = fsDescriptionLink;
        }

        @Override
        public String toString() {
            return "ClassPojo [fsFacilityBanner = " + fsFacilityBanner + ", fsFacilityName = " + fsFacilityName + ", fiFacilityId = " + fiFacilityId + ", fsFacilityicon = " + fsFacilityicon + ", fsDescriptionLink = " + fsDescriptionLink + "]";
        }
    }

    public class FoMenuList {
        private String fiMenuId;
        private String fiMenuHotel;
        private String fsMenuName;
        private String fsMenuImage;

        public String getFiMenuId() {
            return fiMenuId;
        }

        public void setFiMenuId(String fiMenuId) {
            this.fiMenuId = fiMenuId;
        }

        public String getFiMenuHotel() {
            return fiMenuHotel;
        }

        public void setFiMenuHotel(String fiMenuHotel) {
            this.fiMenuHotel = fiMenuHotel;
        }

        public String getFsMenuName() {
            return fsMenuName;
        }

        public void setFsMenuName(String fsMenuName) {
            this.fsMenuName = fsMenuName;
        }

        public String getFsMenuImage() {
            return fsMenuImage;
        }

        public void setFsMenuImage(String fsMenuImage) {
            this.fsMenuImage = fsMenuImage;
        }
    }

    public class FoCatList {
        private String fsTitle;

        private String fiMenuId;

        private String fiCatId;

        private String fsImage;

        public String getFsTitle() {
            return fsTitle;
        }

        public void setFsTitle(String fsTitle) {
            this.fsTitle = fsTitle;
        }

        public String getFiMenuId() {
            return fiMenuId;
        }

        public void setFiMenuId(String fiMenuId) {
            this.fiMenuId = fiMenuId;
        }

        public String getFiCatId() {
            return fiCatId;
        }

        public void setFiCatId(String fiCatId) {
            this.fiCatId = fiCatId;
        }

        public String getFsImage() {
            return fsImage;
        }

        public void setFsImage(String fsImage) {
            this.fsImage = fsImage;
        }

        @Override
        public String toString() {
            return "ClassPojo [fsTitle = " + fsTitle + ", fiMenuId = " + fiMenuId + ", fiCatId = " + fiCatId + ", fsImage = " + fsImage + "]";
        }
    }

}