_Textricator GUI_ is a GUI for [Textricator](https://textricator.mfj.io)

Development is in progress, there have been no releases yet.

_Textricator GUI_ is released under the
[GNU Affero General Public License Version 3](https://www.gnu.org/licenses/agpl-3.0.en.html).

## Quick Start

  - Run `io.mfj.textricator.gui.TextricatorGuiMain`
  - Click the '...' to the right of the "PDF" text box.
  - Open a PDF. The ones in https://github.com/measuresforjustice/textricator/tree/master/src/test/resources/io/mfj/textricator/examples will work.
  - In the "Extract" tab, click the "Extract" button.
  - See your document's text in the "Extracted Text" section.
  - Click on the "Options" section to expand it. Play around with different options to see how it affects the extraction.
  - Parse a PDF with tabular data
    - Open a PDF that has data in tabular layout. E.g.: https://github.com/measuresforjustice/textricator/blob/master/src/test/resources/io/mfj/textricator/examples/probes.pdf
    - Open the "Parse Table" tab.
    - Click the '...' to the right of the "Config file" text box.
    - Select a configuration YaML for the PDF. E.g.: https://raw.githubusercontent.com/measuresforjustice/textricator/master/src/test/resources/io/mfj/textricator/examples/probes.yml
    - Click "Parse".
    - See the parsed data in the "Data" section.
  - Parse a PDF with form data:
    - Open a PDF that has data in a form layout. E.g.: https://github.com/measuresforjustice/textricator/blob/master/src/test/resources/io/mfj/textricator/examples/school-employee-list.pdf
    - Open the "Parse Form" tab.
    - Click the '...' to the right of the "Config file" text box.
    - Select a configuration YaML for the PDF. E.g.: https://raw.githubusercontent.com/measuresforjustice/textricator/master/src/test/resources/io/mfj/textricator/examples/school-employee-list.yml
    - Click "Parse".
    - See the parsed data in the "Data" section.
    - See the log of the parse process in the "Log" section.

This application is actively used and developed by [Measures for Justice](https://measuresforjustice.org).
We welcome feedback, bug reports, and contributions. Create an issue, send a pull request,
or email us at <textricator@mfj.io>.
