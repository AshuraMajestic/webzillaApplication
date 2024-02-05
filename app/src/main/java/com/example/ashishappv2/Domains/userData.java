package com.example.ashishappv2.Domains;

public class userData {
        private String username;
        private String email;
        private String password;
        private String shopname;
        private String address;
        private String number;
        private String link;
        public userData(){};

        public userData(String username, String email, String password, String shopname, String address, String number,String link) {
                this.username = username;
                this.email = email;
                this.password = password;
                this.shopname = shopname;
                this.address = address;
                this.number = number;
                this.link=link;
        }

        public String getLink() {
                return link;
        }

        public void setLink(String link) {
                this.link = link;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
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

        public String getAddress() {
                return address;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public String getNumber() {
                return number;
        }

        public void setNumber(String number) {
                this.number = number;
        }
}
