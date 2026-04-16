class Contact {
    //TODO: Create fields
    private String name;
    private String email;
    private String phoneNumber;
    //Create constructor
    public Contact(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    //Add getters and setters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String toString() {
        //TODO: Return formatted string.
        return "Name: " + name + " | Phone: " + phoneNumber;
    }
}