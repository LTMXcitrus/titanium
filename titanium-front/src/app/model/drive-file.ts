export class DriveFile {
  name: string;
  fileId: string;
  modifiedTime: string;

  constructor(name: string, fileId: string, modifiedTime: string) {
    this.name = name;
    this.fileId = fileId;
    this.modifiedTime = modifiedTime;
  }
}
