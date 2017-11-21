import {Batch} from './batch';

export class InventoryElement {

  name: string;
  more: string;
  perishable: boolean;
  minimum: number;
  stock: number;
  toOrder: number;
  expirationDate: Date;
  location: string;
  tags: String[];
  batch: Batch;
  uptodate: boolean;
  _id: string;

  constructor(name: string, more: string, perishable: boolean, minimum: number, stock: number, toOrder: number, expirationDate: Date, location: string, tags: String[], batch: Batch, uptodate: boolean, id: string = null) {
    this.name = name;
    this.more = more;
    this.perishable = perishable;
    this.minimum = minimum;
    this.stock = stock;
    this.toOrder = toOrder;
    this.expirationDate = expirationDate;
    this.location = location;
    this.tags = tags;
    this.batch = batch;
    this.uptodate = uptodate;
    this._id = id;
  }

}
