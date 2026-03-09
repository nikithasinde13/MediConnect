export class Clinic {
    clinicId: number;
    clinicName: string;
    location: string;
    doctorId: number;
    contactNumber: string;
    establishedYear: number;
  
    constructor(
      clinicId: number,
      clinicName: string,
      location: string,
      doctorId: number,
      contactNumber: string,
      establishedYear: number
    ) {
      this.clinicId = clinicId;
      this.clinicName = clinicName;
      this.doctorId = doctorId;
      this.location = location;
      this.contactNumber = contactNumber;
      this.establishedYear = establishedYear;
    }
  
    logAttributes(): void {
      console.log('clinicId:', this.clinicId);
      console.log('clinicName:', this.clinicName);
      console.log('location:', this.location);
      console.log('contactNumber:', this.contactNumber);
      console.log('establishedYear:', this.establishedYear);
    }
  }
  