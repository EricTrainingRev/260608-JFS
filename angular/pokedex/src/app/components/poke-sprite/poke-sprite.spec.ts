import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PokeSprite } from './poke-sprite';

describe('PokeSprite', () => {
  let component: PokeSprite;
  let fixture: ComponentFixture<PokeSprite>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PokeSprite],
    }).compileComponents();

    fixture = TestBed.createComponent(PokeSprite);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
