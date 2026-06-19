import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PokeLogin } from './poke-login';

describe('PokeLogin', () => {
  let component: PokeLogin;
  let fixture: ComponentFixture<PokeLogin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PokeLogin],
    }).compileComponents();

    fixture = TestBed.createComponent(PokeLogin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
