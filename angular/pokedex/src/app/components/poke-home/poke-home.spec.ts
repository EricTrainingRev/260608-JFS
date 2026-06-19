import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PokeHome } from './poke-home';

describe('PokeHome', () => {
  let component: PokeHome;
  let fixture: ComponentFixture<PokeHome>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PokeHome],
    }).compileComponents();

    fixture = TestBed.createComponent(PokeHome);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
