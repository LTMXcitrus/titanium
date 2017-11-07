import {Batch} from './batch';

export class Element {
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
  _id: string = null) {

  }
}
