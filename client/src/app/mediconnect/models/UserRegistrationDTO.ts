export class UserRegistrationDTO {
    username: string;
    password: string;
    role: string;
    fullName: string;
    contactNumber: string;
    email: string;
    specialty?: string; // Only for doctors
    yearsOfExperience?: number; // Only for doctors
    dateOfBirth?: Date; // Only for patients
    address?: string; // Only for patients
  
    constructor(
      username: string,
      password: string,
      role: string,
      fullName: string,
      contactNumber: string,
      email: string,
      specialty?: string,
      yearsOfExperience?: number,
      dateOfBirth?: Date,
      address?: string
    ) {
      this.username = username;
      this.password = password;
      this.role = role;
      this.fullName = fullName;
      this.contactNumber = contactNumber;
      this.email = email;
      this.specialty = specialty;
      this.yearsOfExperience = yearsOfExperience;
      this.dateOfBirth = dateOfBirth;
      this.address = address;
    }
  
    logAttributes(): void {
      console.log('role:', this.role);
      console.log('username:', this.username);
      console.log('password:', this.password);
      console.log('fullName:', this.fullName);
      console.log('dateOfBirth:', this.dateOfBirth);
      console.log('contactNumber:', this.contactNumber);
      console.log('email:', this.email);
      console.log('address:', this.address);
      console.log('specialty:', this.specialty);
      console.log('yearsOfExperience:', this.yearsOfExperience);
    }
  }
  