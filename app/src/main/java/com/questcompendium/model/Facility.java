package com.questcompendium.model;

public class Facility {

    private FoFacilityList[] foFaciliyList;

    private String fbIsSuccess;

    public FoFacilityList[] getFoFacilityList() {
        return foFaciliyList;
    }

    public void setFoFacilityList(FoFacilityList[] foFacilityList) {
        this.foFaciliyList = foFacilityList;
    }

    public String getFbIsSuccess() {
        return fbIsSuccess;
    }

    public void setFbIsSuccess(String fbIsSuccess) {
        this.fbIsSuccess = fbIsSuccess;
    }

    @Override
    public String toString() {
        return "ClassPojo [foFacilityList = " + foFaciliyList + ", fbIsSuccess = " + fbIsSuccess + "]";
    }

    public static class FoFacilityList {

        private String fsFacilityName;
        private String fiFacilityId;
        private String fsDescriptionLink;
        private String fsFacilityicon;
        private String fsFacilityBanner;

        public String getFsFacilityName() {
            return fsFacilityName;
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

        public String getFsDescriptionLink() {
            return fsDescriptionLink;
        }

        public void setFsDescriptionLink(String fsDescriptionLink) {
            this.fsDescriptionLink = fsDescriptionLink;
        }

        public String getFacilityicon() {
            return fsFacilityicon;
        }

        public void setFacilityicon(String fsFacilityicon) {
            this.fsFacilityicon = fsFacilityicon;
        }

        public String getFsFacilityBanner() {
            return fsFacilityBanner;
        }

        public void setFsFacilityBanner(String fsFacilityBanner) {
            this.fsFacilityBanner = fsFacilityBanner;
        }

        @Override
        public String toString() {
            return "ClassPojo [fsFacilityName = " + fsFacilityName + ", fiFacilityId = " + fiFacilityId + ", fsDescriptionLink = " + fsDescriptionLink + "]";
        }
    }
}

