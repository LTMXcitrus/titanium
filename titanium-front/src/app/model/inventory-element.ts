import {Batch} from './batch';

export class InventoryElement {
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
              uptodate: boolean,
              _id: string = null) {

  }
}
