package com.sdgvvk.v1.modal;

public class Customer {
    private String profileId;
    private String name;
    private String firstname;
    private String middlename;
    private String lastname;
    private String email;
    private String mobile1;
    private String mobile2;
    private String mobile3;
    private String mobile4;
    private String gender;
    private String creationdate;
    private String creationsource;
    private String discontinue;
    private String activepackageid;
    private String profilephotoaddress;

    private String b64;
    private String age;
    private String biodataaddress;
    private String height;
    private String birthtime;
    private String caste;

    private String religion;
    private String education;
    private String occupation;
    private String zodiac;
    private String birthname;
    private String bloodgroup;
    private String property;
    private String fathername;
    private String mothername;
    private String address;
    private String city;
    private String state;
    private String marriagestatus;
    private String birthdate;
    private String birthday;
    private String birthplace;
    private String income;
    private String kuldaivat;
    private String devak;
    private String nakshatra;
    private String nadi;
    private String gan;
    private String yoni;
    private String charan;
    private String gotra;
    private String varn;
    private String mangal;
    private String relatives;

    private String family;
    private String expectations;

    private String relation1;
    private String relationname1;

    private String relation2;
    private String relationname2;
    private String isAdmin;

    private String is_verified;



    public Customer(){

    }



    public Customer(String firstname ,String middlename , String lastname , String mobile , String email, String gender , String birthdate,String is_verified,
                    String status , String height , String education , String occupation , String income , String zodiac , String religion ,
                    String caste,String bloodgroup , String address , String city){
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.mobile1 = mobile;
        this.email = email;
        this.gender = gender;
        this.birthdate = birthdate;
        this.is_verified = is_verified;
        this.marriagestatus = status;
        this.height = height;
        this.education = education;
        this.occupation = occupation;
        this.income = income;
        this.zodiac = zodiac;
        this.religion = religion;
        this.caste = caste;
        this.address = address;
        this.bloodgroup = bloodgroup;
        this.city = city;

    }


    public Customer(String creationSource, String profilePhotoAddress, String biodataAddress,
                    String firstName, String middleName, String lastName, String email,
                    String mobile1, String mobile2,String mobile3, String mobile4, String gender, String height,
                    String birthtime, String caste,String religion, String education, String occupation,
                    String zodiac, String birthName, String bloodGroup, String property,
                    String fatherName, String motherName, String address, String city,
                    String marriageStatus, String birthdate, String birthplace,
                    String income, String kuldaivat, String devak, String nakshatra, String nadi,
                    String gan, String yoni, String charan, String gotra, String varn,
                    String mangal, String expectations,String relation1, String relation2,
                    String relationname1, String relationname2, String relatives,String family) {
        this.creationsource = creationSource;
        this.profilephotoaddress = profilePhotoAddress;
        this.biodataaddress = biodataAddress;
        this.firstname = firstName;
        this.middlename = middleName;
        this.lastname = lastName;
        this.email = email;
        this.mobile1 = mobile1;
        this.mobile2 = mobile2;
        this.mobile3 = mobile3;
        this.mobile4 = mobile4;
        this.gender = gender;
        this.height = height;
        this.birthtime = birthtime;
        this.caste = caste;
        this.religion = religion;
        this.education = education;
        this.occupation = occupation;
        this.zodiac = zodiac;
        this.birthname = birthName;
        this.bloodgroup = bloodGroup;
        this.property = property;
        this.fathername = fatherName;
        this.mothername = motherName;
        this.address = address;
        this.city = city;
        this.marriagestatus = marriageStatus;
        this.birthdate = birthdate;
        this.birthplace = birthplace;
        this.income = income;
        this.kuldaivat = kuldaivat;
        this.devak = devak;
        this.nakshatra = nakshatra;
        this.nadi = nadi;
        this.gan = gan;
        this.yoni = yoni;
        this.charan = charan;
        this.gotra = gotra;
        this.varn = varn;
        this.mangal = mangal;
        this.expectations = expectations;
        this.relation1 = relation1;
        this.relation2 = relation2;
        this.relationname1 = relationname1;
        this.relationname2 = relationname2;
        this.relatives = relatives;
        this.family = family;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(String creationdate) {
        this.creationdate = creationdate;
    }

    public String getCreationsource() {
        return creationsource;
    }

    public void setCreationsource(String creationsource) {
        this.creationsource = creationsource;
    }

    public String getDiscontinue() {
        return discontinue;
    }

    public void setDiscontinue(String discontinue) {
        this.discontinue = discontinue;
    }

    public String getActivepackageid() {
        return activepackageid;
    }

    public void setActivepackageid(String activepackageid) {
        this.activepackageid = activepackageid;
    }

    public String getProfilephotoaddress() {
        return profilephotoaddress;
    }

    public void setProfilephotoaddress(String profilephotoaddress) {
        this.profilephotoaddress = profilephotoaddress;
    }

    public String getBiodataaddress() {
        return biodataaddress;
    }

    public void setBiodataaddress(String biodataaddress) {
        this.biodataaddress = biodataaddress;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBirthtime() {
        return birthtime;
    }

    public void setBirthtime(String birthtime) {
        this.birthtime = birthtime;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public String getBirthname() {
        return birthname;
    }

    public void setBirthname(String birthname) {
        this.birthname = birthname;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getMothername() {
        return mothername;
    }

    public void setMothername(String mothername) {
        this.mothername = mothername;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMarriagestatus() {
        return marriagestatus;
    }

    public void setMarriagestatus(String marriagestatus) {
        this.marriagestatus = marriagestatus;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getKuldaivat() {
        return kuldaivat;
    }

    public void setKuldaivat(String kuldaivat) {
        this.kuldaivat = kuldaivat;
    }

    public String getDevak() {
        return devak;
    }

    public void setDevak(String devak) {
        this.devak = devak;
    }

    public String getNakshatra() {
        return nakshatra;
    }

    public void setNakshatra(String nakshatra) {
        this.nakshatra = nakshatra;
    }

    public String getNadi() {
        return nadi;
    }

    public void setNadi(String nadi) {
        this.nadi = nadi;
    }

    public String getGan() {
        return gan;
    }

    public void setGan(String gan) {
        this.gan = gan;
    }

    public String getYoni() {
        return yoni;
    }

    public void setYoni(String yoni) {
        this.yoni = yoni;
    }

    public String getCharan() {
        return charan;
    }

    public void setCharan(String charan) {
        this.charan = charan;
    }

    public String getGotra() {
        return gotra;
    }

    public void setGotra(String gotra) {
        this.gotra = gotra;
    }

    public String getVarn() {
        return varn;
    }

    public void setVarn(String varn) {
        this.varn = varn;
    }

    public String getMangal() {
        return mangal;
    }

    public void setMangal(String mangal) {
        this.mangal = mangal;
    }

    public String getRelatives() {
        return relatives;
    }

    public void setRelatives(String relatives) {
        this.relatives = relatives;
    }

    public String getExpectations() {
        return expectations;
    }

    public void setExpectations(String expectations) {
        this.expectations = expectations;
    }

    public String getRelation1() {
        return relation1;
    }

    public void setRelation1(String relation1) {
        this.relation1 = relation1;
    }

    public String getRelationname1() {
        return relationname1;
    }

    public void setRelationname1(String relationname1) {
        this.relationname1 = relationname1;
    }

    public String getRelation2() {
        return relation2;
    }

    public void setRelation2(String relation2) {
        this.relation2 = relation2;
    }

    public String getRelationname2() {
        return relationname2;
    }

    public void setRelationname2(String relationname2) {
        this.relationname2 = relationname2;
    }



    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getMobile3() {
        return mobile3;
    }

    public void setMobile3(String mobile3) {
        this.mobile3 = mobile3;
    }

    public String getMobile4() {
        return mobile4;
    }

    public void setMobile4(String mobile4) {
        this.mobile4 = mobile4;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public String getB64() {
        return b64;
    }

    public void setB64(String b64) {
        this.b64 = b64;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
