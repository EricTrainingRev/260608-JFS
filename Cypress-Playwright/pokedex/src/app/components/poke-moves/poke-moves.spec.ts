import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PokeMoves } from './poke-moves';

describe('PokeMoves', () => {
  let component: PokeMoves;
  let fixture: ComponentFixture<PokeMoves>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PokeMoves]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PokeMoves);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
