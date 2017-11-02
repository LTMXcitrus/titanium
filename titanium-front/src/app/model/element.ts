import {Batch} from "./batch";
import {ClosetLocation} from "./closet-location";
export class Element {
  constructor(name: string,
  more: string,
  perishable: boolean,
  minimum: number,
  stock: number,
  expirationDate: Date,
  location: ClosetLocation,
  tags: String[],
  batch: Batch,
  _id: string = null){

  }
}
