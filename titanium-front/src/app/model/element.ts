import {Batch} from './batch';

export class Element {
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
  obsolete: boolean;
  _id: string = null;

  constructor(name: string,
              more: string,
              perishable: boolean,
              minimum: number,
              stock: number,
              toOrder: number,
              expirationDate: Date,
              location: string,
              tags: String[],
              batch: Batch,
              obsolete: boolean,
              id: string) {
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
    this.obsolete = obsolete;
    this._id = id;
  }
}
