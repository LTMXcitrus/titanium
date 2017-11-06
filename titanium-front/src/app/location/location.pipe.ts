import { Pipe, PipeTransform } from '@angular/core';
import {LocationService} from './location.service';

@Pipe({
  name: 'location'
})
export class LocationPipe implements PipeTransform {

  constructor(private locationService: LocationService) {}

  transform(name: string, args?: any): string {
    return this.locationService.getLocationFromName(name).value;
  }

}
