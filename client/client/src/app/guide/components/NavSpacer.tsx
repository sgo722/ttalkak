import React from "react";

const NavSpacer = () => {
  return (
    <>
      <div className="h-10 lg:hidden" aria-hidden="true"></div>
      <div className="h-[60px] hidden lg:block" aria-hidden="true"></div>
    </>
  );
};

export default NavSpacer;
