package com.example.ashishappv2.Domains;

public class userData {
        private String email;
        private String password;
        private String shopname;
        private String number;
        private String link;
        private Boolean online;
        public userData(){};

        public userData( String email, String password, String shopname, String number,String link,Boolean online) {

                this.email = email;
                this.password = password;
                this.shopname = shopname;

                this.number = number;
                this.link=link;
                this.online=online;
        }

        public String getLink() {
                return link;
        }

        public void setLink(String link) {
                this.link = link;
        }



        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getShopname() {
                return shopname;
        }

        public void setShopname(String shopname) {
                this.shopname = shopname;
        }

        public String getNumber() {
                return number;
        }

        public void setNumber(String number) {
                this.number = number;
        }

        public Boolean getOnline() {
                return online;
        }

        public void setOnline(Boolean online) {
                this.online = online;
        }
}
