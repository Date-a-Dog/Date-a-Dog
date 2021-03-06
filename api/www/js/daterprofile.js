/*
 * daterprofile.js is a module encapsulating a user profile.
 *
 */
var DaterProfile = function(fName, lName, email, phone, address) {
    // dater profile properties
    var profile = {};
    profile.fName = fName;
    profile.lName = lName;
    profile.email = email;
    profile.phone = phone;
    profile.address = address;

    return profile;
}

/* Export this file only when testing */
if (typeof module !== "undefined" && typeof module.exports !== "undefined") {
    module.exports = DaterProfile;
} else {
    window.DaterProfile = DaterProfile;
}
