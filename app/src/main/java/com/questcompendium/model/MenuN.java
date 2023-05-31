package com.questcompendium.model;

import java.util.ArrayList;

public class MenuN {
    private ArrayList<FoMenuBarList> foMenuBarList;

    private String fbIsSuccess;

    public ArrayList<FoMenuBarList> getFoMenuBarList() {
        return foMenuBarList;
    }

    public void setFoMenuBarList(ArrayList<FoMenuBarList> foMenuBarList) {
        this.foMenuBarList = foMenuBarList;
    }

    public String getFbIsSuccess() {
        return fbIsSuccess;
    }

    public void setFbIsSuccess(String fbIsSuccess) {
        this.fbIsSuccess = fbIsSuccess;
    }

    @Override
    public String toString() {
        return "ClassPojo [foMenuBarList = " + foMenuBarList + ", fbIsSuccess = " + fbIsSuccess + "]";
    }


    public class FoMenuBarList {
        private String fiDecLink;

        private String fiMenuTitle;

        private String fiPageId;

        public String getFiDecLink() {
            return fiDecLink;
        }

        public void setFiDecLink(String fiDecLink) {
            this.fiDecLink = fiDecLink;
        }

        public String getFiMenuTitle() {
            return fiMenuTitle;
        }

        public void setFiMenuTitle(String fiMenuTitle) {
            this.fiMenuTitle = fiMenuTitle;
        }

        public String getFiPageId() {
            return fiPageId;
        }

        public void setFiPageId(String fiPageId) {
            this.fiPageId = fiPageId;
        }

        @Override
        public String toString() {
            return "ClassPojo [fiDecLink = " + fiDecLink + ", fiMenuTitle = " + fiMenuTitle + ", fiPageId = " + fiPageId + "]";
        }
    }
}
