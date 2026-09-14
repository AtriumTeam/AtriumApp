"""Generate Atrium Sign — custom display face for ATRIUM wordmark and version."""

from __future__ import annotations

from pathlib import Path

from fontTools.fontBuilder import FontBuilder
from fontTools.pens.ttGlyphPen import TTGlyphPen

UPM = 1000
ASC = 820
DESC = -180
CAP = 740
STEM = 150
CHAMFER = 28


def pentagon_chamfer_rect(pen: TTGlyphPen, x: float, y: float, w: float, h: float, c: float = CHAMFER) -> None:
    c = min(c, w / 2.4, h / 2.4)
    pen.moveTo((x + c, y))
    pen.lineTo((x + w - c, y))
    pen.lineTo((x + w, y + c))
    pen.lineTo((x + w, y + h - c))
    pen.lineTo((x + w - c, y + h))
    pen.lineTo((x + c, y + h))
    pen.lineTo((x, y + h - c))
    pen.lineTo((x, y + c))
    pen.closePath()


def poly(pen: TTGlyphPen, pts: list[tuple[float, float]]) -> None:
    pen.moveTo(pts[0])
    for p in pts[1:]:
        pen.lineTo(p)
    pen.closePath()


def draw_notdef(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 80, 0, 420, CAP, 16)
    pentagon_chamfer_rect(pen, 80 + STEM, STEM, 420 - 2 * STEM, CAP - 2 * STEM, 8)


def draw_A(pen: TTGlyphPen) -> None:
    # Architectural A: heavy legs, high bar, chopped apex.
    poly(
        pen,
        [
            (20, 0),
            (20 + STEM, 0),
            (20 + STEM + 40, 250),
            (20 + 40, 250),
        ],
    )
    poly(
        pen,
        [
            (620, 0),
            (620 - STEM, 0),
            (620 - STEM - 40, 250),
            (620 - 40, 250),
        ],
    )
    poly(
        pen,
        [
            (20 + 28, 250),
            (620 - 28, 250),
            (360 + 70, CAP),
            (360 - 70, CAP),
        ],
    )
    pentagon_chamfer_rect(pen, 175, 250, 370, 95, 12)


def draw_T(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 20, CAP - STEM, 600, STEM, 18)
    pentagon_chamfer_rect(pen, 245, 0, STEM, CAP - STEM + 8, 16)


def draw_R(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 40, 0, STEM, CAP, 16)
    pentagon_chamfer_rect(pen, 40, CAP - STEM, 430, STEM, 16)
    pentagon_chamfer_rect(pen, 40, 330, 360, 95, 12)
    pentagon_chamfer_rect(pen, 470 - 30, 425, STEM - 10, CAP - 425, 12)
    poly(
        pen,
        [
            (300, 330),
            (300 + STEM, 330),
            (620, 0),
            (620 - STEM - 10, 0),
        ],
    )


def draw_I(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 40, CAP - 90, 220, 90, 14)
    pentagon_chamfer_rect(pen, 75, 90, STEM, CAP - 180, 16)
    pentagon_chamfer_rect(pen, 40, 0, 220, 90, 14)


def draw_U(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 40, 140, STEM, CAP - 140, 16)
    pentagon_chamfer_rect(pen, 520, 140, STEM, CAP - 140, 16)
    pentagon_chamfer_rect(pen, 40, 0, 630, 170, 22)


def draw_M(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 20, 0, STEM, CAP, 16)
    pentagon_chamfer_rect(pen, 700, 0, STEM, CAP, 16)
    poly(
        pen,
        [
            (20, CAP),
            (20 + STEM, CAP),
            (445, 220),
            (445 - STEM + 20, 220),
        ],
    )
    poly(
        pen,
        [
            (850, CAP),
            (850 - STEM, CAP),
            (445, 220),
            (445 + STEM - 20, 220),
        ],
    )


def draw_digit_stem_pair(pen: TTGlyphPen, open_bottom: bool = False, open_top: bool = False) -> None:
    pentagon_chamfer_rect(pen, 60, 0 if not open_bottom else 120, STEM, CAP if not (open_bottom or open_top) else CAP - 120, 14)


def draw_0(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 50, 0, 480, CAP, 22)
    pentagon_chamfer_rect(pen, 50 + STEM, STEM, 480 - 2 * STEM, CAP - 2 * STEM, 10)


def draw_1(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 210, 0, STEM, CAP, 16)
    poly(pen, [(80, CAP - 180), (210, CAP), (210 + STEM, CAP), (80 + STEM, CAP - 180)])
    pentagon_chamfer_rect(pen, 80, 0, 420, 90, 12)


def draw_2(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 40, CAP - STEM, 500, STEM, 16)
    pentagon_chamfer_rect(pen, 390, 360, STEM, CAP - 360 - STEM + 8, 12)
    pentagon_chamfer_rect(pen, 40, 300, 500, 95, 12)
    pentagon_chamfer_rect(pen, 40, 0, STEM, 300, 12)
    pentagon_chamfer_rect(pen, 40, 0, 500, 95, 12)


def draw_3(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 40, CAP - STEM, 500, STEM, 16)
    pentagon_chamfer_rect(pen, 40, 320, 430, 95, 12)
    pentagon_chamfer_rect(pen, 40, 0, 500, STEM, 16)
    pentagon_chamfer_rect(pen, 390, STEM, STEM, CAP - 2 * STEM, 12)


def draw_4(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 390, 0, STEM, CAP, 16)
    pentagon_chamfer_rect(pen, 40, 280, 500, 95, 12)
    poly(pen, [(40, 280), (40 + STEM, 280), (390, CAP), (390 - 40, CAP)])


def draw_5(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 40, CAP - STEM, 500, STEM, 16)
    pentagon_chamfer_rect(pen, 40, 320, STEM, CAP - 320 - STEM + 8, 12)
    pentagon_chamfer_rect(pen, 40, 320, 500, 95, 12)
    pentagon_chamfer_rect(pen, 390, 0, STEM, 320 + 20, 12)
    pentagon_chamfer_rect(pen, 40, 0, 500, STEM, 16)


def draw_6(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 40, 0, STEM, CAP, 16)
    pentagon_chamfer_rect(pen, 40, CAP - STEM, 500, STEM, 16)
    pentagon_chamfer_rect(pen, 40, 320, 500, 95, 12)
    pentagon_chamfer_rect(pen, 40, 0, 500, STEM, 16)
    pentagon_chamfer_rect(pen, 390, 0, STEM, 320 + 20, 12)


def draw_7(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 40, CAP - STEM, 500, STEM, 16)
    poly(pen, [(390, 0), (390 + STEM, 0), (540, CAP - STEM), (540 - STEM, CAP - STEM)])


def draw_8(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 50, 0, 480, CAP, 20)
    pentagon_chamfer_rect(pen, 50 + STEM - 10, STEM, 480 - 2 * STEM + 20, 210, 8)
    pentagon_chamfer_rect(pen, 50 + STEM - 10, 430, 480 - 2 * STEM + 20, 160, 8)


def draw_9(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 390, 0, STEM, CAP, 16)
    pentagon_chamfer_rect(pen, 40, CAP - STEM, 500, STEM, 16)
    pentagon_chamfer_rect(pen, 40, 320, 500, 95, 12)
    pentagon_chamfer_rect(pen, 40, 320, STEM, CAP - 320, 12)


def draw_period(pen: TTGlyphPen) -> None:
    pentagon_chamfer_rect(pen, 40, 0, 140, 140, 18)


def make_glyph(draw) -> object:
    pen = TTGlyphPen(glyphSet=None)
    draw(pen)
    return pen.glyph()


def empty_glyph():
    pen = TTGlyphPen(glyphSet=None)
    return pen.glyph()


GLYPHS = {
    ".notdef": (draw_notdef, 580),
    "space": (None, 260),
    "A": (draw_A, 720),
    "T": (draw_T, 640),
    "R": (draw_R, 660),
    "I": (draw_I, 300),
    "U": (draw_U, 710),
    "M": (draw_M, 890),
    "zero": (draw_0, 580),
    "one": (draw_1, 540),
    "two": (draw_2, 580),
    "three": (draw_3, 580),
    "four": (draw_4, 580),
    "five": (draw_5, 580),
    "six": (draw_6, 580),
    "seven": (draw_7, 560),
    "eight": (draw_8, 580),
    "nine": (draw_9, 580),
    "period": (draw_period, 220),
}

CMAP = {
    32: "space",
    ord("A"): "A",
    ord("T"): "T",
    ord("R"): "R",
    ord("I"): "I",
    ord("U"): "U",
    ord("M"): "M",
    ord("a"): "A",
    ord("t"): "T",
    ord("r"): "R",
    ord("i"): "I",
    ord("u"): "U",
    ord("m"): "M",
    ord("0"): "zero",
    ord("1"): "one",
    ord("2"): "two",
    ord("3"): "three",
    ord("4"): "four",
    ord("5"): "five",
    ord("6"): "six",
    ord("7"): "seven",
    ord("8"): "eight",
    ord("9"): "nine",
    ord("."): "period",
}


def main() -> None:
    out = Path(__file__).resolve().parents[1] / "core" / "designsystem" / "src" / "main" / "res" / "font" / "atrium_sign_bold.ttf"
    out.parent.mkdir(parents=True, exist_ok=True)

    order = list(GLYPHS.keys())
    glyf = {}
    metrics = {}
    for name, (drawer, width) in GLYPHS.items():
        g = make_glyph(drawer) if drawer else empty_glyph()
        glyf[name] = g
        lsb = getattr(g, "xMin", 0) or 0
        metrics[name] = (width, lsb)

    fb = FontBuilder(UPM, isTTF=True)
    fb.setupGlyphOrder(order)
    fb.setupCharacterMap(CMAP)
    fb.setupGlyf(glyf)
    fb.setupHorizontalMetrics(metrics)
    fb.setupHorizontalHeader(ascent=ASC, descent=DESC, lineGap=40)
    fb.setupNameTable(
        {
            "familyName": "Atrium Sign",
            "styleName": "Bold",
            "uniqueFontIdentifier": "ir.atrium.AtriumSign-Bold",
            "fullName": "Atrium Sign Bold",
            "psName": "AtriumSign-Bold",
            "version": "Version 1.000",
            "copyright": "Atrium Sign. Made for ATRIUM.",
        }
    )
    fb.setupOS2(
        sTypoAscender=ASC,
        sTypoDescender=DESC,
        sTypoLineGap=40,
        usWinAscent=ASC,
        usWinDescent=-DESC,
        sxHeight=520,
        sCapHeight=CAP,
    )
    fb.setupPost()
    fb.save(out)
    print(f"wrote {out}")


if __name__ == "__main__":
    main()
