import { TestBed } from '@angular/core/testing';

import { Pokeservice } from './pokeservice';

describe('Pokeservice', () => {
  let service: Pokeservice;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Pokeservice);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
