package com.questcompendium.model;

public class Category {

    private FoCatList[] foCatList;

    private String fbIsSuccess;

    public FoCatList[] getFoCatList() {
        return foCatList;
    }

    public void setFoCatList(FoCatList[] foCatList) {
        this.foCatList = foCatList;
    }

    public String getFbIsSuccess() {
        return fbIsSuccess;
    }

    public void setFbIsSuccess(String fbIsSuccess) {
        this.fbIsSuccess = fbIsSuccess;
    }

    public static class FoCatList {

        private String fiMenuId;
        private String fiCount;
        private String fsTitle;
        private String fiCatId;
        private String fsImage;

        public String getFiMenuId() {
            return fiMenuId;
        }

        public void setFiMenuId(String fiMenuId) {
            this.fiMenuId = fiMenuId;
        }

        public String getFiCount() {
            return fiCount;
        }

        public void setFiCount(String fiCount) {
            this.fiCount = fiCount;
        }

        public String getFsTitle() {
            return fsTitle;
        }

        public void setFsTitle(String fsTitle) {
            this.fsTitle = fsTitle;
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
    }
}